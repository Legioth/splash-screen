package org.vaadin.leif.splashscreen.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.leif.splashscreen.SplashScreen;
import org.vaadin.leif.splashscreen.SplashScreenHandler;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

@Theme("valo")
@SplashScreen(value = "splash.html", width = 500, height = 300, autohide = false)
public class NoAutocloseDemo extends UI {
    @WebServlet(value = { "/noautohide",
            "/noautohide/*" }, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = NoAutocloseDemo.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        SplashScreenDemoLayout layout = new SplashScreenDemoLayout();
        layout.addComponent(
                new Button("Hide splash screen", new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        SplashScreenHandler.hide();
                        event.getButton().setEnabled(false);
                    }
                }));

        setContent(layout);
    }
}