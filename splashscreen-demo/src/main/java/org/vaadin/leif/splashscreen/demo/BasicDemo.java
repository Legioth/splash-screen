package org.vaadin.leif.splashscreen.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.leif.splashscreen.SplashScreen;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("valo")
@SplashScreen(value = "splash.html", width = 500, height = 300)
public class BasicDemo extends UI {
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = BasicDemo.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(new SplashScreenDemoLayout());
    }
}