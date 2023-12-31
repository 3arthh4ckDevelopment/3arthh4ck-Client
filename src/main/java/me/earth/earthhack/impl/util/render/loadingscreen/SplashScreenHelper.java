package me.earth.earthhack.impl.util.render.loadingscreen;

import me.earth.earthhack.forge.util.ForgeSplashHelper;

public class SplashScreenHelper {
    public static void setSplashScreen(String message, int steps) {
        ForgeSplashHelper.push(message, steps);
    }

    public static void setSubStep(String message) {
        ForgeSplashHelper.setSubStep(message);
    }

    public static void clear() {
        ForgeSplashHelper.clear();
    }

}
