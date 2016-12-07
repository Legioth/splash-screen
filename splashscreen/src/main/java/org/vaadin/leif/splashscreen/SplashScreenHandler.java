package org.vaadin.leif.splashscreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * Bootstrap listener that looks for {@link SplashScreen} annotations on UI
 * classes and includes the configured splash screen on the bootstrap page.
 * <p>
 * Use {@link #init(VaadinService)} inside
 * {@link VaadinServlet#init(javax.servlet.ServletConfig)} to put the listener
 * into use.
 */
public class SplashScreenHandler implements BootstrapListener {
    private static final String BOOTSTRAP_INJECTOR_NAME = "bootstrapInjector.js";

    private static final String INJECTOR_SCRIPT = readInjectorScript();

    /**
     * Keeps track of {@link VaadinService} instances for which the splash
     * screen handler has already been initialized.
     */
    private static final ConcurrentHashMap<VaadinService, Boolean> initedServices = new ConcurrentHashMap<>();

    /**
     * Installs a splash screen handler into the given Vaadin service instance.
     * No-op if the splash screen handler has already been added for the
     * provided service.
     *
     * @param service
     *            the vaadin service instance to use
     */
    public static void init(VaadinService service) {
        initedServices.computeIfAbsent(service, s -> {
            SplashScreenHandler handler = new SplashScreenHandler();
            s.addSessionInitListener(
                    event -> event.getSession().addBootstrapListener(handler));
            s.addServiceDestroyListener(e -> initedServices.remove(s));

            // Dummy value, using the map as a set
            return Boolean.TRUE;
        });
    }

    /**
     * Hides the splash screen for the current UI if it is still visible.
     */
    public static void hide() {
        hide(UI.getCurrent());
    }

    /**
     * Hides the splash screen for the given UI if it is still visible.
     *
     * @param ui
     *            the UI for which the splash screen should be hidden
     */
    public static void hide(UI ui) {
        ui.getPage().getJavaScript().execute(
                "var splash = document.getElementById('splash'); splash && splash.remove()");
    }

    private static String readInjectorScript() {
        InputStream resourceAsStream = SplashScreenHandler.class
                .getResourceAsStream(BOOTSTRAP_INJECTOR_NAME);
        if (resourceAsStream == null) {
            throw new RuntimeException(
                    "Couldn't find " + BOOTSTRAP_INJECTOR_NAME);
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resourceAsStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Couldn't read " + BOOTSTRAP_INJECTOR_NAME, e);
        } finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // Nothing to do here
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Class<? extends UI> uiClass = response.getUiClass();
        SplashScreen splashScreen = uiClass.getAnnotation(SplashScreen.class);
        if (splashScreen == null) {
            // UI not annotated, ignore
            return;
        }

        String fileName = splashScreen.value();

        Element splashScreenHolder = response.getDocument().body()
                .prependElement("div");
        StringBuilder styleBuilder = new StringBuilder("position: absolute;");

        int height = splashScreen.height();
        if (height > 0) {
            styleBuilder.append("top:50%;");
            styleBuilder.append("height:").append(height).append("px;");
            styleBuilder.append("margin-top:-").append(height / 2)
                    .append("px;");
        } else {
            styleBuilder.append("top:0;bottom:0;");
        }

        int width = splashScreen.width();
        if (width > 0) {
            styleBuilder.append("left:50%;");
            styleBuilder.append("width:").append(width).append("px;");
            styleBuilder.append("margin-left:-").append(width / 2)
                    .append("px;");
        } else {
            styleBuilder.append("left:0;right:0;");
        }

        splashScreenHolder.attr("id", "splash").attr("style",
                styleBuilder.toString());

        // Inject splash screen HTML into the beginning of the body
        List<Node> splashContents = getSplashContents(uiClass, fileName);
        for (Node content : splashContents) {
            splashScreenHolder.appendChild(content);
        }

        // Remove default loading indicator
        response.getDocument().body().select(".v-app-loading").remove();

        Elements scripts = response.getDocument().body().select("script");
        String splashParam = response.getRequest().getParameter("splash");
        if (splashParam != null) {
            if ("notheme".equals(splashParam)) {
                // Remove all scripts to prevent vaadinBootstrap from adding the
                // theme
                for (Element script : scripts) {
                    if (!script.hasAttr("src")) {
                        script.remove();
                    }
                }
            } else {
                // Disable application initialization
                findBootstapScripTag(scripts).after(
                        "<script type='text/javascript'>window.vaadin.registerWidgetset = function() {}</script>");
            }
        } else if (splashScreen.autohide()) {
            // Inject splash hider right after the bootstrap js has been loaded
            Element bootstrapScriptTag = findBootstapScripTag(scripts);
            Element bootstrapInjector = new Element(Tag.valueOf("script"), "");
            bootstrapInjector.attr("type", "text/javascript");
            bootstrapInjector.appendChild(new DataNode(INJECTOR_SCRIPT, ""));
            bootstrapScriptTag.after(bootstrapInjector);
        }
    }

    private Element findBootstapScripTag(Elements scripts) {
        for (Element element : scripts) {
            String attr = element.attr("src");
            if (attr != null && attr.contains("vaadinBootstrap.js")) {
                return element;
            }
        }
        throw new RuntimeException("vaadinBootstrap.js script tag not found");
    }

    private List<Node> getSplashContents(Class<? extends UI> uiClass,
            String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return getSplashContentsHtml(uiClass, fileName);
        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")) {
            return getSplashContentsImage(fileName);
        } else {
            throw new RuntimeException(
                    "Unsupported file extension for: " + fileName);
        }

    }

    private List<Node> getSplashContentsImage(String fileName) {
        return Collections.<Node> singletonList(
                new Element(Tag.valueOf("img"), "").attr("src", fileName));
    }

    private List<Node> getSplashContentsHtml(Class<? extends UI> uiClass,
            String fileName) {
        InputStream splashResource = uiClass.getResourceAsStream(fileName);
        if (splashResource == null) {
            throw new RuntimeException("Couldn't find splash screen file "
                    + fileName + " for " + uiClass.getName());
        }
        try {
            Document parse = Jsoup.parse(splashResource, "UTF-8", "");

            List<Node> contents = new ArrayList<Node>(
                    parse.head().childNodes());

            contents.addAll(parse.body().childNodes());

            return contents;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read splash screen file "
                    + fileName + " for " + uiClass.getName(), e);
        } finally {
            try {
                splashResource.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
