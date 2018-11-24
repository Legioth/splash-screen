package org.vaadin.leif.splashscreen.demo;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

// Emulate a slow application start
@HtmlImport("/slow.html?duration=2000")
public class SplashScreenDemoLayout extends VerticalLayout {
    public SplashScreenDemoLayout() {

        H1 heading = new H1("Splash Screen demo");

        Span note = new Span(
                "Nothing here since the splash screen has already disappeared.");

        Html links = new Html("<div>Some variations to check out:<ul>"
                + "<li><a href='.'>Basic</a></li>"
                + "<li><a href='image'>Image</a></li>"
                + "<li><a href='custom'>Custom generator</a></li>"
                + "<li><a href='noautohide'>Without autohide</a></li>"
                + "<li><a href='.?splash'>Basic without loading the applciation</a></li>"
                + "<li><a href='.?splash=notheme'>Basic without loading the applciation or theme</a></li>"
                //
                + "</ul> </div>");

        add(heading, note, links);
    }
}
