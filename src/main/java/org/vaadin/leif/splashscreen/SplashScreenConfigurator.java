package org.vaadin.leif.splashscreen;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a custom splash screen configurator to use for the annotated UI. The
 * configurator is used to define the content and other aspects of the splash
 * screen that is shown when the UI is being loaded.
 * <p>
 * If this annotation is not present on a UI class, splash screen configuration
 * will instead be based on a {@link SplashScreen @SplashScreen} annotation, if
 * present.
 * <p>
 * When a UI is bootstrapped, an instance of the defined configurator class is
 * created using {@link Class#newInstance()}. This means that the class must
 * have a public no-args constructor. If the class is an inner class, the class
 * must also be defined as <code>static</code>.
 */
@Documented
@Retention(RUNTIME)
@Inherited
@Target(TYPE)
public @interface SplashScreenConfigurator {
    /**
     * Defines the splash screen configurator class to use.
     *
     * @return the configurator class to use
     */
    public Class<? extends Configurator> value();
}
