package me.earth.earthhack;

import java.awt.*;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("https://github.com/3arthh4ckDevelopment/3arthh4ck-Client/docs/FAQ.md"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
