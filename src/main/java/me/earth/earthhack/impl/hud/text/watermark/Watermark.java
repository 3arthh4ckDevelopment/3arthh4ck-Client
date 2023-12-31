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
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class Watermark extends HudElement {

    protected final Setting<String> logoText =
            register(new StringSetting("TextLogo", Earthhack.NAME));
    private final Setting<Boolean> showVersion =
            register(new BooleanSetting("Version", true));
    private final Setting<Boolean> sync =
            register(new BooleanSetting("RainbowSync", true));
    private final Setting<Color> color =
            register(new ColorSetting("Color", new Color(33, 150, 243)));

    private String text = "";

    private void render() {
        text = logoText.getValue() + (showVersion.getValue() ? " - " + Earthhack.VERSION : "");
        GlStateManager.pushMatrix();
        if (sync.getValue())
            HudRenderUtil.renderText(text, getX(), getY());
        else if (Caches.getModule(HudEditor.class).get().shadow.getValue())
            Managers.TEXT.drawStringWithShadow(text, getX(), getY(), color.getValue().getRGB());
        else
            Managers.TEXT.drawString(text, getX(), getY(), color.getValue().getRGB());
        GlStateManager.popMatrix();
    }

    public Watermark() {
        super("Watermark", HudCategory.Text, 2, 2);
        this.setData(new SimpleHudData(this, "Displays a watermark."));
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

}
