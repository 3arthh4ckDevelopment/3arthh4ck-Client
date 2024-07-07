package me.earth.earthhack.impl.hud.visual.inventory;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.awt.*;

public class Inventory extends HudElement {

    private final Setting<Boolean> xCarry =
            register(new BooleanSetting("XCarry", false));
    private final Setting<HudBox> box =
            register(new EnumSetting<>("Box", HudBox.Regular));
    private final Setting<Color> boxColor =
            register(new ColorSetting("BoxColor", new Color(23,23,23,23)));
    private final Setting<Color> outlineColor =
            register(new ColorSetting("OutlineColor", new Color(23,23,23,23)));


    protected void onRender() {
        GlStateManager.pushMatrix();
        if (box.getValue() != HudBox.None) {
            if (box.getValue().equals(HudBox.Rounded))
                Render2DUtil.roundedRect(getX(), getY() - 1.0f, getX() + 9 * 18, getY() + 55.0f, 2, boxColor.getValue().getRGB());
            else
                Render2DUtil.drawBorderedRect(getX(), getY(), getX() + 9 * 18, getY() + 55.0f, 1.0f, boxColor.getValue().getRGB(), outlineColor.getValue().getRGB());
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        ItemRender(mc.player.inventory.mainInventory, (int) getX(), (int) getY(), xCarry.getValue());
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void ItemRender(NonNullList<ItemStack> items, int x, int y, boolean xCarry) {
        mc.getRenderItem().zLevel = getZ();
        for (int i = 0; i < items.size() - 9; i++) {
            int iX = x + (i % 9) * (18);
            int iY = y + (i / 9) * (18);
            ItemStack itemStack = items.get(i + 9);
            if (!itemStack.isEmpty())
                Render2DUtil.drawItem(itemStack, iX, iY, true);
        }

        if (xCarry) { // draw the box for xCarry?
            for (int i = 1; i < 5; i++) {
                int iX = x + ((i + 4) % 9) * (18);
                ItemStack itemStack = mc.player.inventoryContainer.inventorySlots.get(i).getStack();
                if (!itemStack.isEmpty())
                    Render2DUtil.drawItem(itemStack, iX, y, true);
            }
        }
        mc.getRenderItem().zLevel = 0.0f;
    }

    public Inventory() {
        super("Inventory", "Displays your inventory",  HudCategory.Visual, 80, 100);
    }

    @Override
    public float getWidth() {
        return 9 * 18;
    }

    @Override
    public float getHeight() {
        return 55.0f;
    }

    public enum HudBox {
        Regular,
        Rounded,
        None
    }
}
