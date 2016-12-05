package org.vaadin.leif.splashscreen.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.leif.splashscreen.SplashScreen;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("valo")
@SplashScreen(value = "VAADIN/splash.jpg", width = 500, height = 330)
public class ImageDemo extends UI {
    @WebServlet(value = "/image", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = ImageDemo.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(new SplashScreenDemoLayout());
    }
}