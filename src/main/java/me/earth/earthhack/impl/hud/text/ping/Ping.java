package me.earth.earthhack.impl.hud.text.ping;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.network.ServerUtil;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;

public class Ping extends HudElement {

    private final Setting<Boolean> showMs =
            register(new BooleanSetting("ShowMs", true));
    private final Setting<String> name =
            register(new StringSetting("CustomName", "Ping"));

    private String ping = "";

    protected void onRender() {
        ping = name.getValue() + " " + TextColor.GRAY + ServerUtil.getPing() + (showMs.getValue() ? "ms" : "");

        HudRenderUtil.renderText(ping, getX(), getY());
    }

    public Ping() {
        super("Ping", "Displays your ping.", HudCategory.Text, 30, 30);
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(ping.trim());
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }
}
