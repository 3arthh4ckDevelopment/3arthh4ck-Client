package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.render.Render3DEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.misc.choruscontrol.util.ESPMode;
import me.earth.earthhack.impl.util.render.Interpolation;
import me.earth.earthhack.impl.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ListenerRender extends ModuleListener<ChorusControl, Render3DEvent> {
    public ListenerRender(ChorusControl module){
        super(module, Render3DEvent.class);
    }
    // TODO: Fix render bugs...
    public void invoke(Render3DEvent e)
    {
        if(mc.player == null) return;
        if(mc.world == null) return;
        if(!module.cancelled) return;
        if(!module.esp.getValue()) return;

        double x = module.tpX - mc.getRenderManager().viewerPosX;
        double y = module.tpY - mc.getRenderManager().viewerPosY;
        y += module.yAnimations.getValue() * (System.currentTimeMillis() - module.time) / module.pulseCycle.getValue().doubleValue();
        double z = module.tpZ - mc.getRenderManager().viewerPosZ;

        AxisAlignedBB bb = Interpolation.interpolateAxis(mc.player.getEntityBoundingBox());
        BlockPos tpPosition = new BlockPos(module.tpX, module.tpY, module.tpZ);

        final float maxBoxAlpha = module.color.getAlpha();
        final float maxOutlineAlpha = module.outline.getAlpha();
        final float alphaBoxAmount = maxBoxAlpha / module.pulseCycle.getValue();
        final float alphaOutlineAmount = maxOutlineAlpha / module.pulseCycle.getValue();
        final int fadeBoxAlpha = MathHelper.clamp((int) (alphaBoxAmount * (module.time + module.pulseCycle.getValue() - System.currentTimeMillis())),0, (int) maxBoxAlpha);
        final int fadeOutlineAlpha = MathHelper.clamp((int) (alphaOutlineAmount * (module.time + module.pulseCycle.getValue() - System.currentTimeMillis())),0, (int) maxOutlineAlpha);
        Color fill;
        Color outline;

        if(module.pulseCycle.getValue() > 0)
        {
            fill = new Color(module.color.getRed(), module.color.getGreen(), module.color.getBlue(), fadeBoxAlpha);
            outline = new Color(module.outline.getRed(), module.outline.getGreen(), module.outline.getBlue(), fadeOutlineAlpha);
        }
        else
        {
            fill = new Color(module.color.getRed(), module.color.getGreen(), module.color.getBlue(), module.color.getAlpha());
            outline = new Color(module.outline.getRed(), module.outline.getGreen(), module.outline.getBlue(), module.outline.getAlpha());
        }

        RenderUtil.startRender();
        if(module.espMode.getValue() == ESPMode.Chams)
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(180 - 90, 0, 1, 0);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);

            double widthX = bb.maxX - bb.minX + 1;
            double widthZ = bb.maxZ - bb.minZ + 1;

            GlStateManager.scale(widthX, bb.maxY - bb.minY, widthZ);
            GlStateManager.translate(0.0F, -1.501F, 0.0F);
            RenderUtil.color(fill);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            module.model.render(0.0625f);

            RenderUtil.color(outline);
            GL11.glLineWidth(module.lineWidth.getValue());
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            module.model.render(0.0625f);
            GL11.glPopMatrix();
        }
        else
            RenderUtil.renderBox(tpPosition, fill, (float)(bb.maxY - bb.minY), fill.getAlpha());
        RenderUtil.endRender();
    }
}
