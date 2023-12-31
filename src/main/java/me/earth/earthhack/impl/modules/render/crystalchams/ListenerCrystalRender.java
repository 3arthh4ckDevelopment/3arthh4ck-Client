package me.earth.earthhack.impl.modules.render.crystalchams;

import me.earth.earthhack.impl.event.events.render.CrystalRenderEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.render.handchams.modes.ChamsMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ListenerCrystalRender extends ModuleListener<CrystalChams, CrystalRenderEvent.Pre> {

    public ListenerCrystalRender(CrystalChams module)
    {
        super(module, CrystalRenderEvent.class);
    }

    @Override
    public void invoke(CrystalRenderEvent.Pre event) {
        if (!module.texture.getValue()) {
            event.setCancelled(true);
        }

        if (module.mode.getValue() == ChamsMode.Gradient) {
            glPushAttrib(GL_ALL_ATTRIB_BITS);
            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisable(GL_TEXTURE_2D);
            float alpha = module.color.getValue().getAlpha() / 255.0f;
            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount() * module.spinSpeed.getValue(),
                    event.getAgeInTicks() * module.bounceFactor.getValue(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            glEnable(GL_TEXTURE_2D);

            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            float f = (float) event.getEntity().ticksExisted + mc.getRenderPartialTicks();
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/rainbow.png"));
            mc.entityRenderer.setupFogColor(true);
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);

            for (int i = 0; i < 2; ++i)
            {
                GlStateManager.disableLighting();
                GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
                GlStateManager.rotate(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 0.5F);
                GlStateManager.translate(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
                GlStateManager.matrixMode(5888);
                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount() * module.spinSpeed.getValue(),
                        event.getAgeInTicks() * module.bounceFactor.getValue(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            }

            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            mc.entityRenderer.setupFogColor(false);
            glPopAttrib();
        } else {
            if (module.wireframe.getValue()) {
                Color wireColor = module.wireFrameColor.getValue();
                glPushAttrib(GL_ALL_ATTRIB_BITS);
                glEnable(GL_BLEND);
                glDisable(GL_TEXTURE_2D);
                glDisable(GL_LIGHTING);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                glLineWidth(module.lineWidth.getValue());
                if (module.wireWalls.getValue()) {
                    glDepthMask(false);
                    glDisable(GL_DEPTH_TEST);
                }

                glColor4f(wireColor.getRed() / 255.0f,
                        wireColor.getGreen() / 255.0f,
                        wireColor.getBlue() / 255.0f,
                        wireColor.getAlpha() / 255.0f);
                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount() * module.spinSpeed.getValue(),
                        event.getAgeInTicks() * module.bounceFactor.getValue(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
                glPopAttrib();
            }

            if (module.chams.getValue()) {
                Color chamsColor = module.color.getValue();
                glPushAttrib(GL_ALL_ATTRIB_BITS);
                glEnable(GL_BLEND);
                glDisable(GL_TEXTURE_2D);
                glDisable(GL_LIGHTING);
                glDisable(GL_ALPHA_TEST);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glEnable(GL_STENCIL_TEST);
                glEnable(GL_POLYGON_OFFSET_LINE);
                if (module.throughWalls.getValue()) {
                    glDepthMask(false);
                    glDisable(GL_DEPTH_TEST);
                }
                glColor4f(chamsColor.getRed() / 255.0f,
                        chamsColor.getGreen() / 255.0f,
                        chamsColor.getBlue() / 255.0f,
                        chamsColor.getAlpha() / 255.0f);
                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount() * module.spinSpeed.getValue(),
                        event.getAgeInTicks() * module.bounceFactor.getValue(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
                glPopAttrib();
            }
        }
    }
}
