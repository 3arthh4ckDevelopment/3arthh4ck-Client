package me.earth.earthhack.forge.mixins.minecraftforge;

import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.util.render.loadingscreen.CustomSplashProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.List;

@Mixin(value = FMLClientHandler.class, remap = false)
public abstract class MixinFMLClientHandler {

    @Redirect(
        method = "beginMinecraftLoading",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/fml/client/SplashProgress;start()V"),
        remap = false)
    public void startScreen(final Minecraft minecraft, final List<IResourcePack> resourcePackList, final IReloadableResourceManager resourceManager, final MetadataSerializer metaSerializer) throws IOException {
        CustomSplashProgress.start();
    }

    @Redirect(
        method = "haltGame",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/fml/client/SplashProgress;finish()V"),
        remap = false)
    public void closeScreen(final String message, final Throwable t) {
        CustomSplashProgress.finish();
    }

    @Redirect(
        method = "finishMinecraftLoading",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/fml/client/SplashProgress;finish()V"),
        remap = false)
    public void closeScreenI() {
        CustomSplashProgress.finish();
        Earthhack.init();
        Earthhack.postInit();
    }

}
