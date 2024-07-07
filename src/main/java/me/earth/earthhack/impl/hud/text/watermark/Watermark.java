package me.earth.earthhack.impl.hud.text.watermark;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.editor.HudEditor;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;

import java.awt.*;

public class Watermark extends HudElement {

    private final Setting<String> logoText =
            register(new StringSetting("TextLogo", Earthhack.NAME));
    private final Setting<Boolean> showVersion =
            register(new BooleanSetting("Version", true));
    private final Setting<Boolean> sync =
            register(new BooleanSetting("RainbowSync", true));
    private final Setting<Color> color =
            register(new ColorSetting("Color", new Color(33, 150, 243)));

    private String text = "";

    protected void onRender() {
        text = logoText.getValue() + (showVersion.getValue() ? " - " + Earthhack.VERSION : "");
        if (sync.getValue())
            HudRenderUtil.renderText(text, getX(), getY());
        else if (Caches.getModule(HudEditor.class).get().shadow.getValue())
            Managers.TEXT.drawStringWithShadow(text, getX(), getY(), color.getValue().getRGB());
        else
            Managers.TEXT.drawString(text, getX(), getY(), color.getValue().getRGB());
    }

    public Watermark() {
        super("Watermark", "Displays a watermark.", HudCategory.Text, 2, 2);
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(text.trim());
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }
}
