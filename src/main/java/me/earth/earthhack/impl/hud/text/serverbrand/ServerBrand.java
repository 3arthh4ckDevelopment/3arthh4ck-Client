package me.earth.earthhack.impl.hud.text.serverbrand;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;

public class ServerBrand extends HudElement {

    private final Setting<String> name =
            register(new StringSetting("BrandText", "ServerBrand"));

    private static String text = "";

    protected void onRender() {
        text = name.getValue() + " " + TextColor.GRAY + mc.player.getServerBrand();
        HudRenderUtil.renderText(text, getX(), getY());
    }

    public ServerBrand() {
        super("ServerBrand", "Displays the server brand.", HudCategory.Text, 35, 50);
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
