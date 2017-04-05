package org.vaadin.leif.splashscreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
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
     * Installs a spash screen handler into the given Vaadin service instance.
     *
     * @param service
     *            the vaadin service instance to use
     */
    public static void init(VaadinService service) {
        final SplashScreenHandler handler = new SplashScreenHandler();
        service.addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent event)
                    throws ServiceException {
                event.getSession().addBootstrapListener(handler);
            }
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
                "var splash = document.getElementById('splash'); splash && splash.parentElement.removeChild(splash);");
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

        Configurator configurator = getConfigurator(uiClass);

        SplashScreenConfiguration configuration = configurator
                .getConfiguration(new SplashScreenEnvironment(uiClass,
                        response.getRequest(), response.getSession()));

        if (configuration == null) {
            // UI not annotated, ignore
            return;
        }

        Element splashScreenHolder = response.getDocument().body()
                .prependElement("div");
        StringBuilder styleBuilder = new StringBuilder("position: absolute;");

        int height = configuration.getHeight();
        if (height > 0) {
            styleBuilder.append("top:50%;");
            styleBuilder.append("height:").append(height).append("px;");
            styleBuilder.append("margin-top:-").append(height / 2)
                    .append("px;");
        } else {
            styleBuilder.append("top:0;bottom:0;");
        }

        int width = configuration.getWidth();
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
        List<Node> splashContents = configuration.getContents();
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
        } else if (configuration.isAutohide()) {
            // Inject splash hider right after the bootstrap js has been loaded
            Element bootstrapScriptTag = findBootstapScripTag(scripts);
            Element bootstrapInjector = new Element(Tag.valueOf("script"), "");
            bootstrapInjector.attr("type", "text/javascript");
            bootstrapInjector.appendChild(new DataNode(INJECTOR_SCRIPT, ""));
            bootstrapScriptTag.after(bootstrapInjector);
        }
    }

    private static Configurator getConfigurator(Class<? extends UI> uiClass) {
        Configurator configurator = null;
        SplashScreenConfigurator configuratorAnnotation = uiClass
                .getAnnotation(SplashScreenConfigurator.class);
        if (configuratorAnnotation == null) {
            configurator = new DefaultConfigurator();
        } else {
            try {
                configurator = configuratorAnnotation.value().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Couldn't instantiate "
                        + configuratorAnnotation.value());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't instantiate "
                        + configuratorAnnotation.value());
            }
        }
        return configurator;
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

}
