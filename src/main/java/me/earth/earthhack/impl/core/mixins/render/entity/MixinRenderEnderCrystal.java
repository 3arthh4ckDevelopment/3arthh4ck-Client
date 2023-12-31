package me.earth.earthhack.impl.core.mixins.render.entity;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.event.events.render.CrystalRenderEvent;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.render.crystalchams.CrystalChams;
import me.earth.earthhack.impl.util.animation.TimeAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RenderEnderCrystal.class)
public abstract class MixinRenderEnderCrystal
    extends Render<EntityEnderCrystal> {
    private static final ModuleCache<CrystalChams> CRYSTAL_CHAMS =
        Caches.getModule(CrystalChams.class);
    @Shadow
    @Final
    private ModelBase modelEnderCrystal;
    @Shadow
    @Final
    private ModelBase modelEnderCrystalNoBase;
    private float scale;

    @Deprecated
    protected MixinRenderEnderCrystal(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(
        method = "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V",
        locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/item/EntityEnderCrystal;shouldShowBottom()Z",
            shift = At.Shift.BEFORE),
        cancellable = true)
    public void preRenderHook(EntityEnderCrystal entity,
                               double x, double y, double z,
                               float entityYaw,
                               float partialTicks,
                               CallbackInfo ci, float f,
                               float f1) {
        if (CRYSTAL_CHAMS.isEnabled()) {
            scale = CRYSTAL_CHAMS.get().animate.getValue() && CRYSTAL_CHAMS.get().scaleMap.containsKey(entity.getEntityId())
                    ? (float) CRYSTAL_CHAMS.get().scaleMap.get(entity.getEntityId()).getCurrent()
                    : CRYSTAL_CHAMS.get().scale.getValue();


            TimeAnimation animation = CRYSTAL_CHAMS.get().scaleMap.get(
                entity.getEntityId());
            if (animation != null) {
                animation.add(Minecraft.getMinecraft().getRenderPartialTicks());
            }

            GlStateManager.scale(scale, scale, scale);
        }

        float limbSwing = 0.0F;
        float limbSwingAmount = f * 3.0F;
        float ageInTicks = f1 * 0.2F;
        float netHeadYaw = 0.0F;
        float headPitch = 0.0F;
        float scale = 0.0625F;

        ModelBase modelBase = modelEnderCrystalNoBase;
        if (Minecraft.getMinecraft().player.dimension == 1 && entity.shouldShowBottom())
            modelBase = modelEnderCrystal;

        RenderEnderCrystal renderLiving = RenderEnderCrystal.class.cast(this);
        CrystalRenderEvent.Pre pre = new CrystalRenderEvent.Pre(renderLiving,
                                                                entity,
                                                                modelBase,
                                                                limbSwing,
                                                                limbSwingAmount,
                                                                ageInTicks,
                                                                netHeadYaw,
                                                                headPitch,
                                                                scale);
        Bus.EVENT_BUS.post(pre);
        if (pre.isCancelled()) {
            CrystalRenderEvent.Post post = new CrystalRenderEvent.Post(
                renderLiving, entity, modelBase, limbSwing, limbSwingAmount,
                ageInTicks, netHeadYaw, headPitch, scale);
            Bus.EVENT_BUS.post(post);

            exitDoRender(entity, x, y, z, entityYaw, partialTicks, f1);
            ci.cancel();
        }
    }

    @Inject(
        method = "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/entity/RenderEnderCrystal;renderOutlines:Z",
            ordinal = 1,
            shift = At.Shift.BEFORE),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void postRenderHook(
        EntityEnderCrystal entity, double x, double y, double z,
        float entityYaw, float partialTicks, CallbackInfo ci,
        float f, float f1) {

        float limbSwingAmount = f * CRYSTAL_CHAMS.get().spinSpeed.getValue();
        float ageInTicks = f1 * CRYSTAL_CHAMS.get().bounceFactor.getValue();
        ModelBase modelBase = modelEnderCrystalNoBase;
        if (Minecraft.getMinecraft().player.dimension == 1 && entity.shouldShowBottom())
            modelBase = modelEnderCrystal;
        RenderEnderCrystal renderLiving = RenderEnderCrystal.class.cast(this);

        CrystalRenderEvent.Post post = new CrystalRenderEvent.Post(
            renderLiving, entity, modelBase, 0.0F, limbSwingAmount,
            ageInTicks, 0.0F, 0.0F, 0.0625F);

        Bus.EVENT_BUS.post(post);

        if (CRYSTAL_CHAMS.isEnabled()) {
            GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
        }
    }

    private void exitDoRender(EntityEnderCrystal entity, double x, double y,
                              double z, float entityYaw, float partialTicks,
                              float f1) {
        if (CRYSTAL_CHAMS.isEnabled()) {
            GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
        }

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        BlockPos blockpos = entity.getBeamTarget();

        if (blockpos != null) {
            this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            float f2 = (float) blockpos.getX() + 0.5F;
            float f3 = (float) blockpos.getY() + 0.5F;
            float f4 = (float) blockpos.getZ() + 0.5F;
            double d0 = (double) f2 - entity.posX;
            double d1 = (double) f3 - entity.posY;
            double d2 = (double) f4 - entity.posZ;
            RenderDragon.renderCrystalBeams(x + d0,
                                            y - 0.3D + (double) (f1 * 0.4F) + d1,
                                            z + d2, partialTicks,
                                            f2, f3, f4, entity.innerRotation,
                                            entity.posX, entity.posY,
                                            entity.posZ);
        }


        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

}
