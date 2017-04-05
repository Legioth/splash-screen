package org.vaadin.leif.splashscreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Node;

/**
 * DTO for the configuration of a splash screen.
 */
public class SplashScreenConfiguration {

    private int height = -1;
    private int width = -1;
    private List<Node> contents = Collections.emptyList();
    private boolean autohide = true;

    /**
     * Gets the configured height.
     *
     * @see #setHeight(int)
     *
     * @return the configured height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the splash screen. If a height is provided, the splash
     * screen will be vertically centered on the page. Otherwise, it will cover
     * the full height of the page. The default value is <code>-1</code>.
     *
     * @param height
     *            the height of the splash screen, in pixels, or -1 to cover the
     *            full height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the configured width.
     *
     * @see #setWidth(int)
     *
     * @return the configured width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the splash screen. If a width is provided, the splash
     * screen will be horizontally centered on the page. Otherwise, it will
     * cover the full width of the page. The default value is <code>-1</code>.
     *
     * @param width
     *            the width of the splash screen, in pixels, or -1 to cover the
     *            full width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets an immutable view of the splash screen contents.
     *
     * @see #setContents(List)
     * @return an immutable list of DOM nodes representing the splash screen
     *         contents.
     */
    public List<Node> getContents() {
        return Collections.unmodifiableList(contents);
    }

    /**
     * Sets a list of DOM nodes to use as the contents of the splash screen.
     *
     * @param contents
     *            a list of DOM nodes to set as the splash screen
     */
    public void setContents(List<Node> contents) {
        this.contents = new ArrayList<Node>(contents);
    }

    /**
     * Gets the autohide setting.
     *
     * @see #setAutohide(boolean)
     * @return the autohide setting
     */
    public boolean isAutohide() {
        return autohide;
    }

    /**
     * Sets whether the splash screen should be hidden automatically when the
     * initial UI contents has been loaded. This is enabled by default.
     * {@link SplashScreenHandler#hide(com.vaadin.ui.UI)} can be used to
     * manually hide the splash screen.
     *
     * @param autohide
     *            <code>true</code> to make the splash screen hide
     *            automatically, <code>false</code> to make it remain visible
     *            until closed manually
     */
    public void setAutohide(boolean autohide) {
        this.autohide = autohide;
    }

}
