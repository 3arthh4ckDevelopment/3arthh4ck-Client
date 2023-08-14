package me.earth.earthhack.impl.hud.serverbrand;

import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;

public class ServerBrand extends HudElement {

    private static String serverBrand = "";

    private void render() {
        EntityPlayerSP player = mc.player;
        if (player != null)
            serverBrand = "ServerBrand " + TextColor.GRAY + player.getServerBrand();
        GlStateManager.pushMatrix();
        HudRenderUtil.renderText(serverBrand, getX(), getY());
        GlStateManager.popMatrix();
    }

    public ServerBrand() {
        super("ServerBrand", 35, 50);
        this.setData(new SimpleHudData(this, "Displays the server brand."));
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
        return Managers.TEXT.getStringWidth(serverBrand);
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }

}
