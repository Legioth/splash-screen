package org.vaadin.leif.splashscreen.demo;

import org.vaadin.leif.splashscreen.SplashScreen;

import com.vaadin.flow.router.Route;

@SplashScreen(value = "VAADIN/splash.jpg", width = 500, height = 330)
@Route("image")
public class ImageDemo extends SplashScreenDemoLayout {
}