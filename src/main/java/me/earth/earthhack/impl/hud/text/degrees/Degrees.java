package me.earth.earthhack.impl.hud.text.degrees;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;

public class Degrees extends HudElement {

    private final Setting<Boolean> pitch =
            register(new BooleanSetting("Pitch", true));
    private final Setting<Boolean> yaw =
            register(new BooleanSetting("Yaw", true));

    private static String text = "";

    protected void onRender() {
        text = "";
        if (pitch.getValue())
            text += String.valueOf(((int) (mc.player.rotationPitch * 100) / 100.0f));
        if (yaw.getValue()) {
            if (pitch.getValue())
                text += " : ";
            text += String.valueOf(((int) (mc.player.rotationYaw * 100) / 100.0f));
        }

        HudRenderUtil.renderText(surroundWithBrackets(text), getX(), getY());
    }

    public Degrees() {
        super("Degrees", "Displays the direction you're currently facing.", HudCategory.Text, 180, 90);
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
