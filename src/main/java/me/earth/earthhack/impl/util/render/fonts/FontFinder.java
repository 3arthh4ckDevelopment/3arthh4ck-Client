package me.earth.earthhack.impl.util.render.fonts;

import java.util.HashMap;
import java.util.Map;

public class FontFinder {
    public static final Map<Character, String> Small;

    static {
        Small = new HashMap<>();
        Small.put('a', "\u1D00");
        Small.put('b', "\u0299");
        Small.put('c', "\u1D04");
        Small.put('d', "\u1D05");
        Small.put('e', "\u1D07");
        Small.put('f', "\uA730");
        Small.put('g', "\u0262");
        Small.put('h', "\u029C");
        Small.put('i', "\u026A");
        Small.put('j', "\u1D0A");
        Small.put('k', "\u1D0B");
        Small.put('l', "\u029F");
        Small.put('m', "\u1D0D");
        Small.put('n', "\u0274");
        Small.put('o', "\u1D0F");
        Small.put('p', "\u1D18");
        Small.put('q', "\uA7AF");
        Small.put('r', "\u0280");
        Small.put('s', "\uA731");
        Small.put('t', "\u1D1B");
        Small.put('u', "\u1D1C");
        Small.put('v', "\u1D20");
        Small.put('w', "\u1D21");
        Small.put('x', "\u036F");
        Small.put('y', "\u028F");
        Small.put('z', "\u1D22");
    }

    public static String getFont(FontEnum font, String string) {
        string = string.toLowerCase();
        StringBuilder builder = new StringBuilder();
        if (font == FontEnum.Small) {
            for (int i = 0; i < string.length(); i++) {
                Character current = string.charAt(i);
                String character = Small.get(current);
                if (character.matches("^[a-zA-Z]*$")) {
                    builder.append(current);
                } else {
                    builder.append(current);
                }

            }
        }
        return null;
    }

    /*
    public static String getFont(FontEnum font, String input) {

        StringBuilder output = new StringBuilder();
        String charValue;
        if (font == FontEnum.Small) {
            for (int i = 0; i < input.length(); i++) {
                charValue = Small.get(input.charAt(i));
                output.append(charValue);
            }
        }
        return output.toString();
    }
    */
}
