package org.vaadin.leif.splashscreen.demo;

import java.util.Collections;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.vaadin.leif.splashscreen.Configurator;
import org.vaadin.leif.splashscreen.SplashScreenConfiguration;
import org.vaadin.leif.splashscreen.SplashScreenConfigurator;
import org.vaadin.leif.splashscreen.SplashScreenEnvironment;
import org.vaadin.leif.splashscreen.demo.CustomGeneratorDemo.DemoConfigurator;

import com.vaadin.flow.router.Route;

@SplashScreenConfigurator(DemoConfigurator.class)
@Route("custom")
public class CustomGeneratorDemo extends SplashScreenDemoLayout {
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
}
