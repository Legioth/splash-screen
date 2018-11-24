package org.vaadin.leif.splashscreen;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

/**
 * Environment in which a a splash screen is to be shown.
 */
public class SplashScreenEnvironment {

    private final Class<? extends HasElement> rootLayoutClass;
    private final VaadinRequest request;
    private final VaadinSession session;

    /**
     * Creates a new instance.
     *
     * @param rootLayoutClass
     *            the root layout class for which the splash screen is shown
     * @param request
     *            the Vaadin request for which the splash screen is shown
     * @param session
     *            the Vaadin session for which the splash screen is shown
     */
    public SplashScreenEnvironment(Class<? extends HasElement> rootLayoutClass,
            VaadinRequest request, VaadinSession session) {
        this.rootLayoutClass = rootLayoutClass;
        this.request = request;
        this.session = session;
    }

    /**
     * Gets the root layout class for which the splash screen is shown.
     *
     * @return the root layout class for which the splash screen is shown
     */
    public Class<? extends HasElement> getRootLayoutClass() {
        return rootLayoutClass;
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
