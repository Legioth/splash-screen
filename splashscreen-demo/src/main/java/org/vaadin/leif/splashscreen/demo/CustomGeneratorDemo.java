package org.vaadin.leif.splashscreen.demo;

import java.util.Collections;

import javax.servlet.annotation.WebServlet;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.vaadin.leif.splashscreen.Configurator;
import org.vaadin.leif.splashscreen.SplashScreenConfiguration;
import org.vaadin.leif.splashscreen.SplashScreenConfigurator;
import org.vaadin.leif.splashscreen.SplashScreenEnvironment;
import org.vaadin.leif.splashscreen.demo.CustomGeneratorDemo.DemoConfigurator;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("valo")
@SplashScreenConfigurator(DemoConfigurator.class)
public class CustomGeneratorDemo extends UI {
    @WebServlet(value = { "/custom", "/custom/*" }, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = CustomGeneratorDemo.class)
    public static class Servlet extends VaadinServlet {
    }

    public static class DemoConfigurator implements Configurator {
        @Override
        public SplashScreenConfiguration getConfiguration(
                SplashScreenEnvironment environment) {
            SplashScreenConfiguration configuration = new SplashScreenConfiguration();

            Node node = new TextNode(
                    "Splash screen for "
                            + environment.getRequest().getHeader("User-Agent"),
                    "");
            configuration.setContents(Collections.singletonList(node));

            return configuration;
        }
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(new SplashScreenDemoLayout());
    }
}
