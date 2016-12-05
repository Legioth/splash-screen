package org.vaadin.leif.splashscreen;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinServiceInitListener;

public class SplashScreenInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent event) {
        SplashScreenHandler.init(event.getSource());
    }
}
