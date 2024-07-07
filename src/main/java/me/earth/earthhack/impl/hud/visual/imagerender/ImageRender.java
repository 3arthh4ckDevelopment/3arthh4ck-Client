package me.earth.earthhack.impl.hud.visual.imagerender;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.ListSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.render.image.EfficientTexture;
import me.earth.earthhack.impl.util.render.image.GifImage;
import me.earth.earthhack.impl.util.render.image.NameableImage;
import me.earth.earthhack.impl.util.text.ChatIDs;
import net.minecraft.client.renderer.GlStateManager;

public class ImageRender extends HudElement {

    private final Setting<Mode> mode =
            register(new EnumSetting<>("Mode", Mode.Image));
    private final Setting<GifImage> gif =
            register(new ListSetting<>("Gif", Managers.FILES.getInitialGif(), Managers.FILES.getGifs()));
    private final Setting<NameableImage> image =
            register(new ListSetting<>("Name", Managers.FILES.getInitialImage(), Managers.FILES.getImages()));
    private final Setting<Float> width =
            register(new NumberSetting<>("Width", 10.0f, 0.0f, 1000.0f));
    private final Setting<Float> height =
            register(new NumberSetting<>("Height", 10.0f, 0.0f, 1000.0f));
    private final Setting<Float> scale =
            register(new NumberSetting<>("Scale", 1.0f, 0.001f, 2.0f));
    private final Setting<Boolean> reload =
            register(new BooleanSetting("Reload", false));

    protected void onRender() {
        float scaleFactor = scale.getValue();
        GlStateManager.scale(scaleFactor, scaleFactor, getZ());
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        if (mode.getValue() == Mode.Image && image.getValue().getTexture() != null) {
            GlStateManager.bindTexture(image.getValue().getTexture().getGlTextureId());
        } else if (mode.getValue() == Mode.Gif && gif.getValue().getDynamicTexture() != null) {
            EfficientTexture texture = gif.getValue().getDynamicTexture();
            if (texture != null) {
                GlStateManager.bindTexture(texture.getGlTextureId());
            }
        }
        Render2DUtil.drawCompleteImage(getX() / scaleFactor, getY() / scaleFactor, width.getValue(), height.getValue());
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
    }

    public ImageRender() {
        super("ImageRender", "Displays an image.",  HudCategory.Visual, 280, 280);

        this.reload.addObserver(event -> {
            event.setCancelled(true);
            Managers.FILES.init();
            Managers.CHAT.sendDeleteMessage("Reloaded resources", this.getName(), ChatIDs.COMMAND);
        });
    }

    @Override
    public float getWidth() {
        return width.getValue() * scale.getValue();
    }

    @Override
    public float getHeight() {
        return height.getValue() * scale.getValue();
    }

    private enum Mode {
        Image,
        Gif
    }
}
