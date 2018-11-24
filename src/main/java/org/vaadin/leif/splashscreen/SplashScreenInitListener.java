package org.vaadin.leif.splashscreen;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

public class SplashScreenInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(new SplashScreenHandler());
    }
}
