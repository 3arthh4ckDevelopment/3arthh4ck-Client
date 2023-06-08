package me.earth.earthhack.impl.modules.client.hud;

import me.earth.earthhack.impl.event.events.render.Render2DEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.text.ChatIDs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

final class ListenerRender extends ModuleListener<HUD, Render2DEvent> {
    public ListenerRender(HUD module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void invoke(Render2DEvent event) {
        module.renderImage();
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        if (module.animations.getValue()) {
            final float ySpeed = 22.f / (Minecraft.getDebugFPS() >> 2);
            if (mc.ingameGUI.getChatGUI().getChatOpen()) {
                if (module.animationY != 14) {
                    if (module.animationY > 14) {
                        module.animationY = Math.max(module.animationY - ySpeed, 14);
                    } else if (module.animationY < 14) {
                        module.animationY = Math.min(module.animationY + ySpeed, 14);
                    }
                }
            } else {
                if (module.animationY != 0) {
                    if (module.animationY > 0) {
                        module.animationY = Math.max(module.animationY - ySpeed, 0);
                    } else if (module.animationY < 0) {
                        module.animationY = Math.min(module.animationY + ySpeed, 0);
                    }
                }
            }
        } else {
            if (mc.ingameGUI.getChatGUI().getChatOpen()) {
                module.animationY = 14;
            } else {
                module.animationY = 0;
            }
        }

        module.renderModules();
        module.skeetLine();
        module.renderLogo();

        if(module.items.getValue())
        {
            switch(module.itemsStyle.getValue())
            {
                case Square:
                    drawSquare(event);
                break;

                case Vertical:
                    drawVertical(event);
                break;

                case Horizontal:
                    drawHorizontal(event);
                break;
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        RenderHelper.disableStandardItemLighting();

        GL11.glPopMatrix();

        if (module.reloadImages.getValue()) {
            module.reloadImages.setValue(false);
            Managers.FILES.init();
            Managers.CHAT.sendDeleteMessage("Reloaded resources", "", ChatIDs.COMMAND);
        } // it isn't very cool here but ok
    }

    private void drawVertical(Render2DEvent event) {
        float x = module.itemsX.getValue() * event.getResolution().getScaledWidth();
        float y = module.itemsY.getValue() * event.getResolution().getScaledHeight();
        int offset = 0;
        int finalOffset = 60;

        if (module.itemsObby.getValue())
            finalOffset += 20;
        if (module.itemsPretty.getValue())
            Render2DUtil.roundedRect(x, y - 1, x + 16, y + finalOffset + 20 - 3, 2.0f, module.itemsColor.getValue().getRGB());
        else
            Render2DUtil.drawRect(x - 3, y - 3, x + 20, y + finalOffset + 20, module.itemsColor.getValue().getRGB());

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

        if (module.itemsObby.getValue()) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Item.getItemById(49), 1), (int) x, (int) y + offset);
            renderItemOverlayIntoGUI(x + 1, y + offset, getItemCount(Item.getItemById(49)));
        }
    }

    private void drawSquare(Render2DEvent event) {
        float x = module.itemsX.getValue() * event.getResolution().getScaledWidth();
        float y = module.itemsY.getValue() * event.getResolution().getScaledHeight();

        if (module.itemsPretty.getValue()) {
            Render2DUtil.roundedRect(x, y, x + 35, y + 35, 4.0f, module.itemsColor.getValue().getRGB());
        } else {
            Render2DUtil.drawRect(x - 3, y - 3, x + 40, y + 40, module.itemsColor.getValue().getRGB());
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
        float x = module.itemsX.getValue() * event.getResolution().getScaledWidth();
        float y = module.itemsY.getValue() * event.getResolution().getScaledHeight();
        int offset = 0;

        // 60 is the final offset value
        int finalOffset = 60;
        if (module.itemsObby.getValue())
            finalOffset += 20;
        if (module.itemsPretty.getValue()) {
            Render2DUtil.roundedRect(x - 2, y + 1, x + finalOffset + 17, y + 18, 2.0f, module.itemsColor.getValue().getRGB());
        } else {
            Render2DUtil.drawRect(x - 2, y - 2, x + finalOffset + 20, y + 19, module.itemsColor.getValue().getRGB());
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

        if (module.itemsObby.getValue()) {
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
