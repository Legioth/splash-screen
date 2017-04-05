package org.vaadin.leif.splashscreen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the splash screen to use for the annotated UI.
 * <p>
 * This annotation is ignored if the UI is also annotated with
 * {@link SplashScreenConfigurator @SplashScreenConfigurator}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface SplashScreen {
    /**
     * The name of the file to use as a splash screen. HTML and images are
     * supported.
     * <p>
     * If the name ends with <code>.html</code>, the name is used to find a HTML
     * file from the classpath, relative to the location of the annotated UI
     * class. The contents of the HTML file is included in the bootstrap HTML
     * page.
     * <p>
     * If the name ends with <code>.png</code> or <code>.jpg</code>, the name is
     * used as-is as the <code>src</code> of an <code>&lt;img&gt;</code> tag on
     * the bootstrap page.
     *
     * @return the file name to use
     */
    String value();

    /**
     * The width of the splash screen. If a width is provided, the splash screen
     * will be centered on the page. Otherwise, it will cover the full width of
     * the page.
     *
     * @return the width of the splash screen, in pixels
     */
    int width() default -1;

    /**
     * The height of the splash screen. If a height is provided, the splash
     * screen will be vertically centered on the page. Otherwise, it will cover
     * the full height of the page.
     *
     * @return the height of the splash screen, in pixels
     */
    int height() default -1;

    /**
     * Determines whether the splash screen should be hidden automatically when
     * the initial UI contents has been loaded. This is enabled by default.
     * {@link SplashScreenHandler#hide(com.vaadin.ui.UI)} can be used to
     * manually hide the splash screen.
     *
     * @return <code>true</code> to make the splash screen hide automatically,
     *         <code>false</code> to make it remain visible until closed
     *         manually
     */
    boolean autohide() default true;
}
