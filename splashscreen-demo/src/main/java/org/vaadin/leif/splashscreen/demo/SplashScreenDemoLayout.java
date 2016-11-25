package org.vaadin.leif.splashscreen.demo;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SplashScreenDemoLayout extends VerticalLayout {
    public SplashScreenDemoLayout() {
        // Emulate a slow application start
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Ignore
        }

        Label heading = new Label("Splash Screen demo");
        heading.addStyleName(ValoTheme.LABEL_H1);

        Label note = new Label(
                "Nothing here since the splash screen has already disappeared.");

        Label links = new Label("Some variations to check out:<ul>"
                + "<li><a href='.'>Basic</a></li>"
                + "<li><a href='image'>Image</a></li>"
                + "<li><a href='noautohide'>Without autohide</a></li>"
                + "<li><a href='.?splash'>Basic without loading the applciation</a></li>"
                + "<li><a href='.?splash=notheme'>Basic without loading the applciation or theme</a></li>"
                //
                + "</ul> ", ContentMode.HTML);

        setMargin(true);
        setSpacing(true);
        addComponents(heading, note, links);
    }
}
