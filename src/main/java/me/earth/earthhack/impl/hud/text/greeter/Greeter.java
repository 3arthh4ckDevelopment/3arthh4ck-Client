package me.earth.earthhack.impl.hud.text.greeter;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.media.Media;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Calendar;

public class Greeter extends HudElement {

    private final Setting<GreeterMode> greeterMode =
            register(new EnumSetting<>("Mode", GreeterMode.LongNew));
    private final Setting<String> custom =
            register(new StringSetting("Custom", "Welcome to Future Beta >:D"));

    public static String text = "Welcome to Future Beta >:D";

    private void render() {
        if (mc.player != null) {
            String name = mc.player.getDisplayName().getFormattedText().trim();
            String playerName = Caches.getModule(Media.class).returnIfPresent(m -> m.convert(name), name);
            switch (greeterMode.getValue()) {
                case LongNew:
                    text = "Welcome to " + Earthhack.NAME + " " + playerName + " :^)";
                    break;
                case LongVer:
                    text = "Welcome to " + Earthhack.NAME + " " + Earthhack.VERSION + " " + playerName + " :^)";
                    break;
                case LongOld:
                    text = "Welcome to Phobos.eu " + playerName + " :^)";
                    break;
                case Time:
                    text = getTimeOfDay() + playerName;
                    break;
                case Weird:
                    text = "Welcome to phobro hack " + playerName;
                    break;
                case Custom:
                    text = custom.getValue().replace("%player%", playerName);
                    break;
                default:
                    text = "Welcome " + playerName;
            }
        }
        GlStateManager.pushMatrix();
        HudRenderUtil.renderText(text, getX(), getY());
        GlStateManager.popMatrix();
    }

    public static String getTimeOfDay() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay < 12){
            return "Good Morning ";
        } else if(timeOfDay < 16){
            return "Good Afternoon ";
        } else if(timeOfDay < 21){
            return "Good Evening ";
        } else {
            return "Good Night ";
        }
    }

    public Greeter() {
        super("Greeter", HudCategory.Text,  (Render2DUtil.getScreenWidth() / 2.0f) - (Managers.TEXT.getStringWidth(text) / 2.0f), 2);
        this.setData(new SimpleHudData(this, "Greets you."));
    }

    @Override
    public void guiDraw(int mouseX, int mouseY, float partialTicks) {
        super.guiDraw(mouseX, mouseY, partialTicks);
        render();
    }

    @Override
    public void hudDraw(float partialTicks) {
        render();
    }

    @Override
    public void guiUpdate(int mouseX, int mouseY, float partialTicks) {
        super.guiUpdate(mouseX, mouseY, partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public void hudUpdate(float partialTicks) {
        super.hudUpdate(partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(text);
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }

    private enum GreeterMode {
        Time,
        LongNew,
        LongVer,
        LongOld,
        Weird,
        Custom,
        Simple
    }

}
