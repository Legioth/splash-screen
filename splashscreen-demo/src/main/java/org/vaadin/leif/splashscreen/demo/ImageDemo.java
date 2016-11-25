package org.vaadin.leif.splashscreen.demo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.vaadin.leif.splashscreen.SplashScreen;
import org.vaadin.leif.splashscreen.SplashScreenHandler;

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
        @Override
        public void init(ServletConfig servletConfig) throws ServletException {
            super.init(servletConfig);

            // Hook up with the framework's host page generation
            SplashScreenHandler.init(getService());
        }
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(new SplashScreenDemoLayout());
    }
}