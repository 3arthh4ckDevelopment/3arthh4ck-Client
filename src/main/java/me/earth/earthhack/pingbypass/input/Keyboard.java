package me.earth.earthhack.pingbypass.input;

import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.pingbypass.PingBypass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Keyboard {
    private static final Map<Integer, Boolean> STATES = new ConcurrentHashMap<>();

    public static int getKeyboardSize() {
        return org.lwjgl.input.Keyboard.KEYBOARD_SIZE;
    }

    public static String getKeyName(int key) {
        if (key > getKeyboardSize() && key < getKeyboardSize() + 10)
            return "MOUSE " + (key - getKeyboardSize());
        return org.lwjgl.input.Keyboard.getKeyName(key);
    }

    public static int getRControl() {
        return org.lwjgl.input.Keyboard.KEY_RCONTROL;
    }

    public static int getLControl() {
        return org.lwjgl.input.Keyboard.KEY_LCONTROL;
    }

    public static int getLMenu() {
        return org.lwjgl.input.Keyboard.KEY_LMENU;
    }

    public static int getEscape() {
        return org.lwjgl.input.Keyboard.KEY_ESCAPE;
    }

    public static int getSpace() {
        return org.lwjgl.input.Keyboard.KEY_SPACE;
    }

    public static int getDelete() {
        return org.lwjgl.input.Keyboard.KEY_DELETE;
    }

    public static int getNone() {
        return org.lwjgl.input.Keyboard.KEY_NONE;
    }

    public static int getKeyV() {
        return org.lwjgl.input.Keyboard.KEY_V;
    }

    public static int getKeyM() {
        return org.lwjgl.input.Keyboard.KEY_M;
    }

    public static void enableRepeatEvents(boolean enable) {
        org.lwjgl.input.Keyboard.enableRepeatEvents(enable);
    }

    public static boolean getEventKeyState() {
        return org.lwjgl.input.Keyboard.getEventKeyState();
    }

    public static int getEventKey() {
        return org.lwjgl.input.Keyboard.getEventKey();
    }

    public static char getEventCharacter() {
        return org.lwjgl.input.Keyboard.getEventCharacter();
    }

    public static int getKeyIndex(String string) {
        if (string.contains("MOUSE ")) {
            Integer parsedKey = Integer.getInteger(string.replace("MOUSE ", "")) + getKeyboardSize();
            if (parsedKey > getKeyboardSize() && parsedKey < getKeyboardSize() + 10) {
                return parsedKey + getKeyboardSize();
            }
        }

        try {
            return org.lwjgl.input.Keyboard.getKeyIndex(string);
        } catch (ArrayIndexOutOfBoundsException e) {
            ChatUtil.sendMessage("Failed to set the bind");
        }
        return 0;
    }

    public static boolean isKeyDown(int code) {
        if (PingBypass.isConnected()) {
            return STATES.getOrDefault(code, false);
        }

        return org.lwjgl.input.Keyboard.isKeyDown(code);
    }

}
