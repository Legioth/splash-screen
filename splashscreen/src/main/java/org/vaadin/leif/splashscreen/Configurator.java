package org.vaadin.leif.splashscreen;

/**
 * Configurator interface for custom splash screen contents. Annotate a UI with
 * {@link SplashScreenConfigurator @SplashScreenConfigurator} to define a
 * {@link Configurator} implementation to use for the splash screen of that UI.
 */
public interface Configurator {

    /**
     * Creates the splash screen configuration to use for the given environment.
     *
     * @param environment
     *            the splash screen environment for which to show a splash
     *            screen
     * @return the splash screen configuration to use, or <code>null</code> to
     *         not show any splash screen
     */
    SplashScreenConfiguration getConfiguration(
            SplashScreenEnvironment environment);

}
