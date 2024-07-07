package me.earth.earthhack.impl.hud.visual.armor;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.minecraft.DamageUtil;
import me.earth.earthhack.impl.util.render.ColorHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

// TODO: Support for Netherite armor on ViaVersion servers
//  since it currently shows negative durability for them...

public class Armor extends HudElement {

    private final Setting<Boolean> durability =
            register(new BooleanSetting("Durability", true));
    private final Setting<Boolean> vertical =
            register(new BooleanSetting("Vertical", false));

    protected void onRender() {
        int offsetValue = 0;
        GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.player.inventory.armorInventory.get(i);
            if (!stack.isEmpty()) {
                if (durability.getValue()) {
                    final float percent = DamageUtil.getPercent(stack) / 100.0f;
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.625f, 0.625f, 0.625f);
                    Managers.TEXT.drawStringWithShadow(
                            ((int) (percent * 100.0f)) + "%", (getX() + (vertical.getValue() ? 1 : offsetValue + 2)) * 1.6f, (getY() + (!vertical.getValue() ? 1 : offsetValue + 2)) * 1.6f, ColorHelper.toColor(percent * 120.0f, 100.0f, 50.0f, 1.0f).getRGB());
                    GlStateManager.scale(1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                }

                GlStateManager.pushMatrix();
                RenderItem itemRender = mc.getRenderItem();
                itemRender.zLevel = getZ();
                itemRender.renderItemAndEffectIntoGUI(mc.player, stack,
                        (int) getX() + (vertical.getValue() ? 4 : offsetValue),
                        (int) getY() + (!vertical.getValue() ? 4 : offsetValue));
                itemRender.zLevel = 0.0f;
                GlStateManager.popMatrix();
                offsetValue += 18;
            }
        }

        RenderHelper.disableStandardItemLighting();
    }

    public Armor() {
        super("Armor", "Displays your armor.", HudCategory.Visual, 120, 420);
    }

    @Override
    public float getWidth() {
        return 72.0f;
    }

    @Override
    public float getHeight() {
        return 20.0f;
    }
}
