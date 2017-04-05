package org.vaadin.leif.splashscreen;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * Environment in which a a splash screen is to be shown.
 */
public class SplashScreenEnvironment {

    private final Class<? extends UI> uiClass;
    private final VaadinRequest request;
    private final VaadinSession session;

    /**
     * Creates a new instance.
     *
     * @param uiClass
     *            the UI class for which the splash screen is shown
     * @param request
     *            the Vaadin request for which the splash screen is shown
     * @param session
     *            the Vaadin session for which the splash screen is shown
     */
    public SplashScreenEnvironment(Class<? extends UI> uiClass,
            VaadinRequest request, VaadinSession session) {
        this.uiClass = uiClass;
        this.request = request;
        this.session = session;
    }

    /**
     * Gets the UI class for which the splash screen is shown.
     *
     * @return the UI class for which the splash screen is shown
     */
    public Class<? extends UI> getUiClass() {
        return uiClass;
    }

    /**
     * Gets the Vaadin request for which the splash screen is shown
     * 
     * @return the Vaadin request for which the splash screen is shown
     */
    public VaadinRequest getRequest() {
        return request;
    }

    /**
     * Gets the Vaadin session for which the splash screen is shown
     * 
     * @return the Vaadin session for which the splash screen is shown
     */
    public VaadinSession getSession() {
        return session;
    }

}
