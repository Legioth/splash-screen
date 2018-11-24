package org.vaadin.leif.splashscreen;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;

/**
 * Bootstrap listener that looks for {@link SplashScreen} annotations on root
 * layout classes and includes the configured splash screen on the bootstrap
 * page.
 */
public class SplashScreenHandler implements BootstrapListener {
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
        ui.getPage().executeJavaScript(
                "debugger; var splash = document.getElementById('splash'); splash && splash.parentElement.removeChild(splash);");
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        List<HasElement> routerChain = response.getUI().getInternals()
                .getActiveRouterTargetsChain();
        if (routerChain.isEmpty()) {
            return;
        }

        Class<? extends HasElement> rootLayoutClass = routerChain.get(0)
                .getClass();

        Configurator configurator = getConfigurator(rootLayoutClass);

        SplashScreenConfiguration configuration = configurator
                .getConfiguration(new SplashScreenEnvironment(rootLayoutClass,
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
            // Hide will actually happen once the UI is fully bootstrapped

            // XXX This doesn't work because the initial UIDL has already been
            // generated at this point
            hide(response.getUI());
        }
    }

    private static Configurator getConfigurator(
            Class<? extends HasElement> rootLayoutClass) {
        Configurator configurator = null;
        SplashScreenConfigurator configuratorAnnotation = rootLayoutClass
                .getAnnotation(SplashScreenConfigurator.class);
        if (configuratorAnnotation == null) {
            configurator = new DefaultConfigurator();
        } else {
            try {
                configurator = configuratorAnnotation.value().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
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
