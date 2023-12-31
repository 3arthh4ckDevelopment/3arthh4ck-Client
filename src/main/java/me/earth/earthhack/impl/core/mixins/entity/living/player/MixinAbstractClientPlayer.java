package me.earth.earthhack.impl.core.mixins.entity.living.player;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.capes.Capes;
import me.earth.earthhack.impl.modules.player.fakeplayer.FakePlayer;
import me.earth.earthhack.impl.modules.render.norender.NoRender;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import me.earth.earthhack.impl.util.thread.LookUpUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer
{
    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Shadow public abstract String getSkinType();

    private static final ModuleCache<NoRender>
        NO_RENDER = Caches.getModule(NoRender.class);
    private static final ModuleCache<Capes>
            CAPES = Caches.getModule(Capes.class);

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    public void getFovModifierHook(CallbackInfoReturnable<Float> info)
    {
        if (NO_RENDER.returnIfPresent(NoRender::dynamicFov, false))
        {
            info.setReturnValue(1.0f);
        }
    }

    @Inject(method= "getLocationCape" , at=@At(value="HEAD"), cancellable=true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (CAPES.isEnabled() && mc.player != null) {
            NetworkPlayerInfo info = this.getPlayerInfo();
            if (info.getGameProfile().getName().equals(mc.player.getName())) {
                UUID uuid = this.getPlayerInfo().getGameProfile().getId();
                if (uuid != null) {
                    callbackInfoReturnable.setReturnValue(CAPES.get().getCapeResource());
                }
            }
        }
    }

    private static final Map<Integer, ResourceLocation> SKIN_MAP = new ConcurrentHashMap<>();
    private static final ModuleCache<FakePlayer> FAKE_PLAYER = Caches.getModule(FakePlayer.class);

    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (PlayerUtil.isFakePlayer(this.getEntityId())) {
            if (SKIN_MAP.containsKey(this.getEntityId())) {
                callbackInfoReturnable.setReturnValue(SKIN_MAP.get(this.getEntityId()));
                return;
            }
            BufferedImage image = LookUpUtil.getSkin(LookUpUtil.getUUID(FAKE_PLAYER.get().name.getValue()));
            if (image == null) {
                Earthhack.getLogger().info("[Skin Yoinker] No skin found");
                return;
            }
            ResourceLocation location = mc.getTextureManager().getDynamicTextureLocation("skin_" + FAKE_PLAYER.get().name.getValue(), new DynamicTexture(image));
            SKIN_MAP.put(this.getEntityId(), location);
            callbackInfoReturnable.setReturnValue(location);
        }
    }

    @Inject(method = "getSkinType", at = @At("HEAD"), cancellable = true)
    public void injectGetSkinType(CallbackInfoReturnable<String> cir) {
        if (SKIN_MAP.containsKey(this.getEntityId())) {
            cir.setReturnValue("classic");
        }
    }
}
