package org.vaadin.leif.splashscreen.demo;

import org.vaadin.leif.splashscreen.SplashScreen;
import org.vaadin.leif.splashscreen.SplashScreenHandler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;

@SplashScreen(value = "splash.html", width = 500, height = 300, autohide = false)
@Route("noautohide")
public class NoAutocloseDemo extends SplashScreenDemoLayout {

    public NoAutocloseDemo() {
        add(new Button("Hide splash screen", event -> {
            SplashScreenHandler.hide();
            event.getSource().setEnabled(false);
        }));
    }

}