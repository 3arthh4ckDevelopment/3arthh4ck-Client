package me.earth.earthhack.impl.hud.totem;

import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Totem extends HudElement {

    private void render() {
        RenderItem itemRender = mc.getRenderItem();
        int totems = InventoryUtil.getCount(Items.TOTEM_OF_UNDYING);

        if (totems > 0) {
            float y = getY();
            itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(mc.player, new ItemStack(Items.TOTEM_OF_UNDYING), (int) getX(), (int) y);
            itemRender.zLevel = 0.0f;
            GlStateManager.disableDepth();
            String text = String.valueOf(totems);
            HudRenderUtil.renderText(text, (int) getX() + 17 - RENDERER.getStringWidth(text), (int) getY() + 9);
            GlStateManager.enableDepth();
        }
    }

    public Totem() {
        super("Totem", 120, 120);
        this.setData(new SimpleHudData(this, "Displays your totems."));
    }

    @Override
    public void guiDraw(int mouseX, int mouseY, float partialTicks) {
        super.guiDraw(mouseX, mouseY, partialTicks);
        int totems = InventoryUtil.getCount(Items.TOTEM_OF_UNDYING);
        if (totems > 0)
            render();
        else {
            GlStateManager.disableDepth();
            HudRenderUtil.renderText("Totem", getX(), getY());
            GlStateManager.enableDepth();
        }

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
        return 18.0f;
    }

    @Override
    public float getHeight() {
        return 18.0f;
    }

}
