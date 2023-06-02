package me.earth.earthhack.impl.modules.render.pvpresources;

import me.earth.earthhack.impl.event.events.render.Render2DEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * 70% of the module made by:
 * @author _kisman_
 * @since 20:03 of 21.08.2022
 */

final class ListenerRender extends ModuleListener<PvpResources, Render2DEvent> {
    public ListenerRender(PvpResources module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void invoke(Render2DEvent event) {
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        if (module.style.getValue() == Styles.Square) {
            drawSquare(event);
        } else if (module.style.getValue() == Styles.Vertical) {
            drawVertical(event);
        } else {
            drawHorizontal(event);
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        RenderHelper.disableStandardItemLighting();
    }

    private void drawVertical(Render2DEvent event) {
        float x = module.x.getValue() * event.getResolution().getScaledWidth();
        float y = module.y.getValue() * event.getResolution().getScaledHeight();
        int offset = 0;

        // 60 is the final offset value
        int finalOffset = 60;
        if (module.obby.getValue())
            finalOffset += 20;
        if (module.pretty.getValue())
            Render2DUtil.roundedRect(x, y - 1, x + 16, y + finalOffset + 20 - 3, 2.0f, module.color.getValue().getRGB());
        else
            Render2DUtil.drawRect(x - 3, y - 3, x + 20, y + finalOffset + 20, module.color.getValue().getRGB());

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(426), 1), (int) x, (int) y + offset);
        renderItemOverlayIntoGUI(x + 1, y + offset, getItemCount(Item.getItemById(426)));
        offset += 20;

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(384), 1), (int) x,  (int) y + offset);
        renderItemOverlayIntoGUI(x + 1, y +  offset, getItemCount(Item.getItemById(384)));
        offset += 20;

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(322), 1), (int) x, (int )y + offset);
        renderItemOverlayIntoGUI(x + 1, y + offset, getItemCount(Item.getItemById(322)));
        offset += 20;

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(449), 1), (int) x, (int) y + offset);
        renderItemOverlayIntoGUI(x + 1, y + offset, getItemCount(Item.getItemById(449)));
        offset += 20;

        if (module.obby.getValue()) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(49), 1), (int) x, (int) y + offset);
            renderItemOverlayIntoGUI(x + 1, y + offset, getItemCount(Item.getItemById(49)));
        }
    }

    private void drawSquare(Render2DEvent event) {
        float x = module.x.getValue() * event.getResolution().getScaledWidth();
        float y = module.y.getValue() * event.getResolution().getScaledHeight();

        if (module.pretty.getValue()) {
            Render2DUtil.roundedRect(x, y, x + 35, y + 35, 4.0f, module.color.getValue().getRGB());
        } else {
            Render2DUtil.drawRect(x - 3, y - 3, x + 40, y + 40, module.color.getValue().getRGB());
        }

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(426), 1), (int) x, (int) y);
        renderItemOverlayIntoGUI(x + 1, y , getItemCount(Item.getItemById(426)));

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(384), 1), (int) x,  (int) y + 20);
        renderItemOverlayIntoGUI(x + 1, y + 20, getItemCount(Item.getItemById(384)));

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(322), 1), (int) x + 20, (int ) y);
        renderItemOverlayIntoGUI(x + 21, y, getItemCount(Item.getItemById(322)));

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(449), 1), (int) x + 20, (int) y + 20);
        renderItemOverlayIntoGUI(x + 21, y + 20, getItemCount(Item.getItemById(449)));
    }

    private void drawHorizontal(Render2DEvent event) {
        float x = module.x.getValue() * event.getResolution().getScaledWidth();
        float y = module.y.getValue() * event.getResolution().getScaledHeight();
        int offset = 0;

        // 60 is the final offset value
        int finalOffset = 60;
        if (module.obby.getValue())
            finalOffset += 20;
        if (module.pretty.getValue()) {
            Render2DUtil.roundedRect(x - 2, y + 1, x + finalOffset + 17, y + 18, 2.0f, module.color.getValue().getRGB());
        } else {
            Render2DUtil.drawRect(x - 2, y - 2, x + finalOffset + 20, y + 19, module.color.getValue().getRGB());
        }

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(426), 1), (int) x + offset, (int) y);
        renderItemOverlayIntoGUI(x + offset + 1, y, getItemCount(Item.getItemById(426)));
        offset += 20;

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(384), 1), (int) x + offset,  (int) y);
        renderItemOverlayIntoGUI(x + offset + 1, y, getItemCount(Item.getItemById(384)));
        offset += 20;

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(322), 1), (int) x + offset, (int ) y);
        renderItemOverlayIntoGUI(x + offset + 1, y, getItemCount(Item.getItemById(322)));
        offset += 20;

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(449), 1), (int) x + offset, (int) y);
        renderItemOverlayIntoGUI(x + offset + 1, y, getItemCount(Item.getItemById(449)));
        offset += 20;

        if (module.obby.getValue()) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(49), 1), (int) x + offset, (int) y);
            renderItemOverlayIntoGUI(x + offset + 1, y, getItemCount(Item.getItemById(49)));
        }
    }

    public static String getItemCount(Item item) {
        int itemCount = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum() + ((mc.player.getHeldItemOffhand().getItem() == item)
                ? mc.player.getHeldItemOffhand().getCount() : 0);
        if (itemCount >= 1000)
            return Integer.toString(itemCount).charAt(0) + "." + Integer.toString(itemCount).charAt(1) + "K";
        else
            return Integer.toString(itemCount);
    }

    public void renderItemOverlayIntoGUI(float xPosition, float yPosition, String s) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        mc.fontRenderer.drawStringWithShadow(s, xPosition + 19 - 2 - mc.fontRenderer.getStringWidth(s), yPosition + 6 + 3, -1);
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }
}
