package me.earth.earthhack.impl.hud.visual.model;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.util.render.Render2DUtil;

public class Model extends HudElement {

    private final Setting<Integer> modelScale =
            register(new NumberSetting<>("ModelScale", 40, 1, 100));

    protected void onRender() {
        Render2DUtil.drawPlayer(mc.player, modelScale.getValue(), (int) (getX() + getWidth() / 2), (int) (getY() + getHeight()));
    }

    public Model() {
        super("Model", "Displays your playermodel.", HudCategory.Visual, 400, 300);
    }

    @Override
    public float getWidth() {
        return 1.0f * modelScale.getValue();
    }

    @Override
    public float getHeight() {
        return 2.0f * modelScale.getValue();
    }
}
