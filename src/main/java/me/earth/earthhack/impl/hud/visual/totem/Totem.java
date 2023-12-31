package me.earth.earthhack.impl.hud.visual.totem;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.impl.gui.hud.HudEditorGui;
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

        itemRender.zLevel = 200.0f;
        itemRender.renderItemAndEffectIntoGUI(mc.player, new ItemStack(Items.TOTEM_OF_UNDYING), (int) getX(), (int) getY());
        itemRender.zLevel = 0.0f;
        GlStateManager.disableDepth();

        if (totems <= 0 && mc.currentScreen instanceof HudEditorGui) {
            HudRenderUtil.renderText("0", (int) getX() + 17 - RENDERER.getStringWidth("0"), (int) getY() + 9);
        } else {
            HudRenderUtil.renderText(String.valueOf(totems), (int) getX() + 17 - RENDERER.getStringWidth(String.valueOf(totems)), (int) getY() + 9);
        }
        GlStateManager.enableDepth();
    }

    public Totem() {
        super("Totem", HudCategory.Visual, 120, 120);
        this.setData(new SimpleHudData(this, "Displays your totems."));
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
        return 18.0f;
    }

    @Override
    public float getHeight() {
        return 18.0f;
    }

}
