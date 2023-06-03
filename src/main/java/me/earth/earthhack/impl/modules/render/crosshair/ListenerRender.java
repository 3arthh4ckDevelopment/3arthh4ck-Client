package me.earth.earthhack.impl.modules.render.crosshair;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.event.events.render.Render2DEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.hud.HUD;
import me.earth.earthhack.impl.modules.client.hud.modes.HudRainbow;
import me.earth.earthhack.impl.modules.combat.autocrystal.AutoCrystal;
import me.earth.earthhack.impl.modules.combat.autotrap.AutoTrap;
import me.earth.earthhack.impl.modules.combat.blocker.Blocker;
import me.earth.earthhack.impl.modules.combat.holefiller.HoleFiller;
import me.earth.earthhack.impl.modules.combat.surround.Surround;
import me.earth.earthhack.impl.modules.render.crosshair.mode.GapMode;
import me.earth.earthhack.impl.util.minecraft.MovementUtil;
import me.earth.earthhack.impl.util.render.ColorUtil;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import org.lwjgl.opengl.GL11;

import static me.earth.earthhack.impl.modules.client.hud.HUD.RENDERER;
import static org.lwjgl.opengl.GL11.glScalef;

/**
 * @author Gerald
 * @since 6/17/2021
 **/

final class ListenerRender extends ModuleListener<CrossHair, Render2DEvent> {

    public ListenerRender(CrossHair module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void invoke(Render2DEvent event) {
        final int screenMiddleX = event.getResolution().getScaledWidth() / 2;
        final int screenMiddleY = event.getResolution().getScaledHeight() / 2;
        final float width = module.width.getValue();

        if (module.crossHair.getValue() && module.outline.getValue()){
            // Top Box
            Render2DUtil.drawBorderedRect(screenMiddleX - width, screenMiddleY - (module.gapSize.getValue() + module.length.getValue()) - ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0), screenMiddleX + (module.width.getValue()), screenMiddleY - (module.gapSize.getValue()) - ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0), 0.5f, module.color.getValue().getRGB(), module.outlineColor.getValue().getRGB());
            // Bottom Box
            Render2DUtil.drawBorderedRect(screenMiddleX - width, screenMiddleY + (module.gapSize.getValue()) + ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue() : 0), screenMiddleX + (module.width.getValue()), screenMiddleY + (module.gapSize.getValue() + module.length.getValue()) + ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0), 0.5f, module.color.getValue().getRGB(), module.outlineColor.getValue().getRGB());
            // Left Box
            Render2DUtil.drawBorderedRect(screenMiddleX - (module.gapSize.getValue() + module.length.getValue()) - ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0), screenMiddleY - (module.width.getValue()), screenMiddleX - (module.gapSize.getValue()) - ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0), screenMiddleY + (module.width.getValue()), 0.5f, module.color.getValue().getRGB(), module.outlineColor.getValue().getRGB());
            // Right Box
            Render2DUtil.drawBorderedRect(screenMiddleX + (module.gapSize.getValue()) + ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0), screenMiddleY - (module.width.getValue()), screenMiddleX + (module.gapSize.getValue() + module.length.getValue()) + ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0), screenMiddleY + (module.width.getValue()), 0.5f, module.color.getValue().getRGB(), module.outlineColor.getValue().getRGB());
        }

        if (module.indicator.getValue()) {
            float f = this.mc.player.getCooledAttackStrength(0.0F);
            float indWidthInc = ((screenMiddleX + (module.gapSize.getValue() + module.length.getValue()) + ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0)) - (screenMiddleX - (module.gapSize.getValue() + module.length.getValue()) - ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0))) / 17.f;
            if (f < 1.0f) {
                final float finWidth = (indWidthInc * (f * 17.f));
                Render2DUtil.drawBorderedRect(screenMiddleX - (module.gapSize.getValue() + module.length.getValue()) - ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0),
                        (screenMiddleY + (module.gapSize.getValue() + module.length.getValue()) + ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0)) + 2,
                        screenMiddleX - (module.gapSize.getValue() + module.length.getValue()) - ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0) + finWidth,
                        (screenMiddleY + (module.gapSize.getValue() + module.length.getValue()) + ((MovementUtil.isMoving() && module.gapMode.getValue() == GapMode.Dynamic) ? module.gapSize.getValue(): 0)) + 2 + (module.width.getValue() * 2),
                        0.5f, module.color.getValue().getRGB(), module.outlineColor.getValue().getRGB());

            }
        }

        if (module.dot.getValue()) {
            Render2DUtil.drawCircle(screenMiddleX, screenMiddleY, module.dotRadius.getValue(), module.dotColor.getValue().getRGB());
        }

        if (module.pvpInfo.getValue()) {
            int offset = module.offset.getValue();
            GL11.glPushMatrix();
            glScalef(module.scale.getValue(), module.scale.getValue(), 1.0f);

            if (module.autoCrystalB.getValue()) {
                offset += renderModule(Caches.getModule(AutoCrystal.class), screenMiddleX, screenMiddleY, offset);
            }
            if (module.autoTrapB.getValue()) {
                offset += renderModule(Caches.getModule(AutoTrap.class), screenMiddleX, screenMiddleY, offset);
            }
            if (module.blockerB.getValue()) {
                offset += renderModule(Caches.getModule(Blocker.class),screenMiddleX, screenMiddleY, offset);
            }
            if (module.holeFillerB.getValue()) {
                offset += renderModule(Caches.getModule(HoleFiller.class), screenMiddleX, screenMiddleY, offset);
            }
            if (module.surroundB.getValue()) {
                offset += renderModule(Caches.getModule(Surround.class), screenMiddleX, screenMiddleY, offset);
            }
            GL11.glPopMatrix();
        }
        //TODO: looks quite bad, needs a rewrite
    }


    private int renderModule(ModuleCache<?> setting, int screenMiddleX, int screenMiddleY, int offset) {
        boolean info = module.info.getValue();
        boolean hudSync = module.hudSync.getValue();
        float textHeight = Managers.TEXT.getStringHeight();
        final ModuleCache<HUD> HUD = Caches.getModule(HUD.class);
        if (setting.isEnabled()) {
            if (hudSync) {
                String colorCode = HUD.get().colorMode.getValue().getColor();
                RENDERER.drawStringWithShadow(colorCode + setting.getDisplayName() + (info && setting.getDisplayInfo() != null ? TextColor.GRAY + " [" + TextColor.WHITE + setting.getDisplayInfo() +TextColor.GRAY + "]" : ""),
                        rescaleTextX(setting, screenMiddleX, info), (screenMiddleY + offset) / module.scale.getValue(),
                        HUD.get().colorMode.getValue() == HudRainbow.None
                                ? HUD.get().color.getValue().getRGB()
                                : (HUD.get().colorMode.getValue() == HudRainbow.Static ? (ColorUtil.staticRainbow((screenMiddleY + offset) / module.scale.getValue() * 0.89f, HUD.get().color.getValue())) : 0xffffffff));

            } else {
                Managers.TEXT.drawStringWithShadow(
                        setting.getDisplayName() + (info && setting.getDisplayInfo() != null ? TextColor.GRAY + " [" + TextColor.WHITE + setting.getDisplayInfo() + TextColor.GRAY + "]" : ""),
                        rescaleTextX(setting, screenMiddleX, info),
                        (screenMiddleY + offset) / module.scale.getValue(),
                        module.PvpInfoColor.getValue().getRGB());
            }

            return (int) (textHeight + module.textDistance.getValue() - 4);
        }
        return 0;
    }

    private float rescaleTextX(ModuleCache<?> setting, int middleX, boolean info) {
        int textWidth = Managers.TEXT.getStringWidth(setting.getDisplayName());
        if (info && setting.getDisplayInfo() != null)
            textWidth += Managers.TEXT.getStringWidth(" [" + setting.getDisplayInfo() + "]");
        return (middleX / module.scale.getValue()) - textWidth / 2.0f;
    }

}
