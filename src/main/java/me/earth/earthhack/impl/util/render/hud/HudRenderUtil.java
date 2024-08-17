package me.earth.earthhack.impl.util.render.hud;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.managers.render.TextRenderer;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.editor.HudEditor;
import me.earth.earthhack.impl.util.render.ColorUtil;
import me.earth.earthhack.impl.util.text.TextColor;

import java.awt.*;

public class HudRenderUtil implements Globals {
    private static final TextRenderer RENDERER = Managers.TEXT;
    private static final ModuleCache<HudEditor> HUD_EDITOR = Caches.getModule(HudEditor.class);

    public static String[] getBrackets() {
        return HUD_EDITOR.get().getBrackets();
    }

    public static String getBracketsColor() {
        if (!(HUD_EDITOR.get().bracketsColor.getValue() == TextColor.None))
            return HUD_EDITOR.get().bracketsColor.getValue().getColor();
        else
            return TextColor.GRAY;
    }

    public static String getBracketsTextColor() {
        if (!(HUD_EDITOR.get().insideText.getValue() == TextColor.None))
            return HUD_EDITOR.get().insideText.getValue().getColor();
        else
            return TextColor.GRAY;
    }

    public static void renderText(String text, float x, float y) {
        renderText(text, x, y, 1.0f);
    }

    public static void renderText(String text, float x, float y, float scale) {
        String colorCode = HUD_EDITOR.get().colorMode.getValue().getColor();
        RENDERER.drawStringScaled(colorCode + text, x, y, textColor(x, y), HUD_EDITOR.get().shadow.getValue(), scale);
    }

    private static int textColor(float x, float y) {
        if (HUD_EDITOR.get().colorMode.getValue() == HudRainbow.None) {
            return HUD_EDITOR.get().color.getValue().getRGB();
        } else if (HUD_EDITOR.get().colorMode.getValue() == HudRainbow.Static) {
            return ColorUtil.staticRainbow((y + 1) * 0.89f, HUD_EDITOR.get().color.getValue());
        } else {
            return Color.HSBtoRGB(
                    Managers.COLOR.getHueByPosition(HUD_EDITOR.get().colorMode.getValue() == HudRainbow.Horizontal ? y : x),
                    1.0f,
                    1.0f);
        }
    }
}
