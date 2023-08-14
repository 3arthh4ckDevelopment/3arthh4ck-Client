package me.earth.earthhack.impl.hud.fps;

import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class FPS extends HudElement {

    private final Setting<String> name =
            register(new StringSetting("CustomName", "Fps "));

    private String fps = name.getValue();

    private void render() {
        if (mc.player != null && mc.world != null)
            fps = name.getValue() + TextColor.GRAY + Minecraft.getDebugFPS();
        GlStateManager.pushMatrix();
        HudRenderUtil.renderText(fps, getX(), getY());
        GlStateManager.popMatrix();
    }

    public FPS() {
        super("FPS", 50, 20);
        this.setData(new SimpleHudData(this, "Displays your FPS"));
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
        return Managers.TEXT.getStringWidth(fps);
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }

}
