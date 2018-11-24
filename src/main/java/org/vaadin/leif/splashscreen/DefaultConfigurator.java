package org.vaadin.leif.splashscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;

/**
 * Default splash screen configurator that is used for root layotus that are not
 * annotated to use a custom configurator. The default configurator looks for a
 * {@link SplashScreen @SplashScreen} annotation for finding how to configure
 * the layout's splash screen.
 */
public class DefaultConfigurator implements Configurator {

    @Override
    public SplashScreenConfiguration getConfiguration(
            SplashScreenEnvironment environment) {
        Class<? extends HasElement> rootLayoutClass = environment
                .getRootLayoutClass();
        SplashScreen splashScreen = rootLayoutClass
                .getAnnotation(SplashScreen.class);

        if (splashScreen == null) {
            return null;
        }

        SplashScreenConfiguration splashScreenConfiguration = new SplashScreenConfiguration();

        String fileName = splashScreen.value();
        splashScreenConfiguration
                .setContents(getSplashContents(rootLayoutClass, fileName));
        splashScreenConfiguration.setWidth(splashScreen.width());
        splashScreenConfiguration.setHeight(splashScreen.height());
        splashScreenConfiguration.setAutohide(splashScreen.autohide());

        return splashScreenConfiguration;
    }

    /**
     * Helper for generating splash screen contents based on the given file name
     * and root layout class.
     * <p>
     * For html files (<code>.html</code> or <code>.htm</code>), the splash
     * screen content is made up by loading the filename from the classpath
     * relative to the provided UI class. For image files (<code>.png</code>,
     * <code>.jpg</code> or <code>.jpeg</code>), the splash screen content is
     * made up of an image tag with the filename as the image's
     * <code>src</code>.
     *
     * @param rootLayoutClass
     *            the root layout class that should used for resolving files
     *            from the classpath
     * @param fileName
     *            the filename to use for the splash screen contents
     * @return a list of DOM nodes that make up the splash screen contents
     */
    public static List<Node> getSplashContents(
            Class<? extends HasElement> rootLayoutClass,
            String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return getSplashContentsHtml(rootLayoutClass, fileName);
        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")) {
            return getSplashContentsImage(fileName);
        } else {
            throw new RuntimeException(
                    "Unsupported file extension for: " + fileName);
        }

    }

    /**
     * Creates the splash screen contents for an image.
     *
     * @param src
     *            the image src
     * @return a list of DOM nodes that make up the image
     */
    public static List<Node> getSplashContentsImage(String src) {
        return Collections.<Node> singletonList(
                new Element(Tag.valueOf("img"), "").attr("src", src));
    }

    /**
     * Loads the given HTML file name from the classpath relative to the given
     * root layout class and returns the file contents as a list of DOM nodes.
     * 
     * @param rootLayoutClass
     *            the root layout class that should used for resolving files
     *            from the classpath
     * @param fileName
     *            the HTML file name to load
     * @return a list of DOM nodes corresponding to the HTML file contents
     */
    public static List<Node> getSplashContentsHtml(
            Class<? extends HasElement> rootLayoutClass,
            String fileName) {
        try (InputStream splashResource = rootLayoutClass
                .getResourceAsStream(fileName)) {
            if (splashResource == null) {
                throw new RuntimeException("Couldn't find splash screen file "
                        + fileName + " for " + rootLayoutClass.getName());
            }

            Document parse = Jsoup.parse(splashResource, "UTF-8", "");

            List<Node> contents = new ArrayList<>(parse.head().childNodes());

            contents.addAll(parse.body().childNodes());

            return contents;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read splash screen file "
                    + fileName + " for " + rootLayoutClass.getName(), e);
        }
    }

}
