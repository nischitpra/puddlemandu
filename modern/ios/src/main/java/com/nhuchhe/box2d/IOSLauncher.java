package com.nhuchhe.box2d;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;

/** Launches the iOS (RoboVM/MobiVM) application. */
public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration configuration = new IOSApplicationConfiguration();
        // This game code uses pixel-sized textures and assumes screen units are pixels.
        // On iOS retina, the default can be "Logical" points (e.g. 480x320) while the
        // backbuffer is larger (e.g. 1440x960), making everything look scaled/wrong.
        configuration.hdpiMode = HdpiMode.Pixels;
        // Match Android manifest: lock to landscape for consistent layout.
        configuration.orientationLandscape = true;
        configuration.orientationPortrait = false;
        return new IOSApplication(new MyGame(), configuration);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}

