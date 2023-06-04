package me.earth.earthhack.impl.core.mixins.render;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.render.popchams.PopChams;
import net.minecraft.client.particle.ParticleTotem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(ParticleTotem.class)
public abstract class MixinParticleTotem extends ParticleSimpleAnimated {

    private static final ModuleCache<PopChams> POPCHAMS = Caches.getModule(PopChams.class);

    protected MixinParticleTotem(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 176, 8, -0.05F);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, CallbackInfo info) {
        if (POPCHAMS.get().particles.getValue() && POPCHAMS.isEnabled()) {
            if (POPCHAMS.get().particlesRandom.getValue() && POPCHAMS.isEnabled())
                this.setRBGColorF(this.rand.nextInt(255), this.rand.nextInt(255), this.rand.nextInt(255));
            else
                this.setRBGColorF(POPCHAMS.get().particlesColor.getValue().getBlue(), POPCHAMS.get().particlesColor.getValue().getGreen(),POPCHAMS.get().particlesColor.getValue().getRed());

            this.setBaseAirFriction(POPCHAMS.get().particleFriction.getValue());

            this.motionX *= POPCHAMS.get().particleDuration.getValue();
            this.motionY *= POPCHAMS.get().particleDuration.getValue();
            this.motionZ *= POPCHAMS.get().particleDuration.getValue();
        } else { // else just vanilla
            this.particleScale *= 0.75F;
            this.particleMaxAge = 60 + this.rand.nextInt(12);

            if (this.rand.nextInt(4) == 0)
            {
                this.setRBGColorF(0.6F + this.rand.nextFloat() * 0.2F, 0.6F + this.rand.nextFloat() * 0.3F, this.rand.nextFloat() * 0.2F);
            }
            else
            {
                this.setRBGColorF(0.1F + this.rand.nextFloat() * 0.2F, 0.4F + this.rand.nextFloat() * 0.3F, this.rand.nextFloat() * 0.2F);
            }

            this.setBaseAirFriction(0.6F);
        }
    }
}
