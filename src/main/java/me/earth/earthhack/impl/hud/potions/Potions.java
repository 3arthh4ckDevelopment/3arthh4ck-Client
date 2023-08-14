package me.earth.earthhack.impl.hud.potions;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.hud.DynamicHudElement;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.editor.HudEditor;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.ColorUtil;
import me.earth.earthhack.impl.util.render.hud.HudRainbow;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;

//TODO: hud potion symbols
public class Potions extends DynamicHudElement {

    private static final ModuleCache<HudEditor> HUD_EDITOR = Caches.getModule(HudEditor.class);

    private final Setting<PotionColor> potionColor =
            register(new EnumSetting<>("PotionColor", PotionColor.Phobos));
    private final Setting<Integer> textOffset =
            register(new NumberSetting<>("Offset", 2, 0, 10));

    private String label = "Potions [you don't have any effect!!]"; // render this?
    int potCounter = 0;

    private void render() {
        if (mc.player != null) {
            final ArrayList<Potion> sorted = new ArrayList<>();
            potCounter = 0;
            for (final Potion potion : Potion.REGISTRY) {
                if (potion != null) {
                    if (mc.player.isPotionActive(potion)) {
                        potCounter++;
                        sorted.add(potion);
                    }
                }
            }
            sorted.sort(Comparator.comparingDouble(potion -> -RENDERER.getStringWidth(I18n.format(potion.getName()) + (mc.player.getActivePotionEffect(potion).getAmplifier() > 0 ? " " + (mc.player.getActivePotionEffect(potion).getAmplifier() + 1) : "") + ChatFormatting.GRAY + " " + Potion.getPotionDurationString(Objects.requireNonNull(mc.player.getActivePotionEffect(potion)), 1.0F))));
            int offset = 0;
            float yPos = (directionV() == TextDirectionV.BottomToTop ? getY() + (sorted.size() * (Managers.TEXT.getStringHeight() + textOffset.getValue())) - Managers.TEXT.getStringHeightI() : getY());
            float borderDistance = simpleCalcH(getWidth());
            for (final Potion potion : sorted) {
                final PotionEffect effect = mc.player.getActivePotionEffect(potion);
                if (effect != null) {
                    final String label = I18n.format(potion.getName()) + (effect.getAmplifier() > 0 ? " " + (effect.getAmplifier() + 1) : "") + ChatFormatting.GRAY + " " + Potion.getPotionDurationString(effect, 1.0F);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    float xPos = getX() - simpleCalcH(RENDERER.getStringWidth(label));
                    if (directionV() == TextDirectionV.BottomToTop)
                        renderPotionText(label, borderDistance + xPos, yPos - offset - animationY, effect.getPotion());
                    else
                        renderPotionText(label, borderDistance + xPos, yPos + offset + animationY, effect.getPotion());
                    offset += RENDERER.getStringHeightI() + textOffset.getValue();
                }
            }
        }
    }

    public void renderPotionText(String text, float x, float y, Potion potion) {
        String colorCode = (potionColor.getValue() == PotionColor.Normal || potionColor.getValue() == PotionColor.Phobos) ? "" : HUD_EDITOR.get().colorMode.getValue().getColor();
        RENDERER.drawStringWithShadow(colorCode + text,
                x,
                y,
                (potionColor.getValue() == PotionColor.Normal || potionColor.getValue() == PotionColor.Phobos) ? potionColorMap.get(potion).getRGB() : (
                        HUD_EDITOR.get().colorMode.getValue() == HudRainbow.None
                                ? HUD_EDITOR.get().color.getValue().getRGB()
                                : (HUD_EDITOR.get().colorMode.getValue() == HudRainbow.Static ? (ColorUtil.staticRainbow((y + 1) * 0.89f, HUD_EDITOR.get().color.getValue())) : 0xffffffff)));
    }

    private final Map<Potion, Color> potionColorMap = new HashMap<>();
    public Potions() {
        super("PotionEffects", 120, 120);
        this.setData(new SimpleHudData(this, "Displays active potion effects."));

        this.potionColor.addObserver(e -> {
            if (potionColor.getValue() == PotionColor.Phobos) {
                potionColorMap.clear();
                potionColorMap.put(MobEffects.SPEED, new Color(85, 255, 255));
                potionColorMap.put(MobEffects.SLOWNESS, new Color(0, 0, 0));
                potionColorMap.put(MobEffects.HASTE, new Color(255, 170, 0));
                potionColorMap.put(MobEffects.MINING_FATIGUE, new Color(74, 66, 23));
                potionColorMap.put(MobEffects.STRENGTH, new Color(255, 85, 85));
                potionColorMap.put(MobEffects.INSTANT_HEALTH, new Color(67, 10, 9));
                potionColorMap.put(MobEffects.INSTANT_DAMAGE, new Color(67, 10, 9));
                potionColorMap.put(MobEffects.JUMP_BOOST, new Color(85, 255, 255));
                potionColorMap.put(MobEffects.NAUSEA, new Color(85, 29, 74));
                potionColorMap.put(MobEffects.REGENERATION, new Color(255, 85, 255));
                potionColorMap.put(MobEffects.RESISTANCE, new Color(255, 85, 85));
                potionColorMap.put(MobEffects.FIRE_RESISTANCE, new Color(255, 170, 0));
                potionColorMap.put(MobEffects.WATER_BREATHING, new Color(46, 82, 153));
                potionColorMap.put(MobEffects.INVISIBILITY, new Color(127, 131, 146));
                potionColorMap.put(MobEffects.BLINDNESS, new Color(31, 31, 35));
                potionColorMap.put(MobEffects.NIGHT_VISION, new Color(85, 255, 85));
                potionColorMap.put(MobEffects.HUNGER, new Color(88, 118, 83));
                potionColorMap.put(MobEffects.WEAKNESS, new Color(0, 0, 0));
                potionColorMap.put(MobEffects.POISON, new Color(85, 255, 85));
                potionColorMap.put(MobEffects.WITHER, new Color(0, 0, 0));
                potionColorMap.put(MobEffects.HEALTH_BOOST, new Color(248, 125, 35));
                potionColorMap.put(MobEffects.ABSORPTION, new Color(85, 85, 255));
            } else if (potionColor.getValue() == PotionColor.Normal) {
                potionColorMap.clear();
                potionColorMap.put(MobEffects.SPEED, new Color(124, 175, 198));
                potionColorMap.put(MobEffects.SLOWNESS, new Color(90, 108, 129));
                potionColorMap.put(MobEffects.HASTE, new Color(217, 192, 67));
                potionColorMap.put(MobEffects.MINING_FATIGUE, new Color(74, 66, 23));
                potionColorMap.put(MobEffects.STRENGTH, new Color(147, 36, 35));
                potionColorMap.put(MobEffects.INSTANT_HEALTH, new Color(67, 10, 9));
                potionColorMap.put(MobEffects.INSTANT_DAMAGE, new Color(67, 10, 9));
                potionColorMap.put(MobEffects.JUMP_BOOST, new Color(34, 255, 76));
                potionColorMap.put(MobEffects.NAUSEA, new Color(85, 29, 74));
                potionColorMap.put(MobEffects.REGENERATION, new Color(205, 92, 171));
                potionColorMap.put(MobEffects.RESISTANCE, new Color(153, 69, 58));
                potionColorMap.put(MobEffects.FIRE_RESISTANCE, new Color(228, 154, 58));
                potionColorMap.put(MobEffects.WATER_BREATHING, new Color(46, 82, 153));
                potionColorMap.put(MobEffects.INVISIBILITY, new Color(127, 131, 146));
                potionColorMap.put(MobEffects.BLINDNESS, new Color(31, 31, 35));
                potionColorMap.put(MobEffects.NIGHT_VISION, new Color(31, 31, 161));
                potionColorMap.put(MobEffects.HUNGER, new Color(88, 118, 83));
                potionColorMap.put(MobEffects.WEAKNESS, new Color(72, 77, 72));
                potionColorMap.put(MobEffects.POISON, new Color(78, 147, 49));
                potionColorMap.put(MobEffects.WITHER, new Color(53, 42, 39));
                potionColorMap.put(MobEffects.HEALTH_BOOST, new Color(248, 125, 35));
                potionColorMap.put(MobEffects.ABSORPTION, new Color(37, 82, 165));
            }

            potionColorMap.put(MobEffects.SATURATION, new Color(248, 36, 35));
            potionColorMap.put(MobEffects.GLOWING, new Color(148, 160, 97));
            potionColorMap.put(MobEffects.LEVITATION, new Color(206, 255, 255));
            potionColorMap.put(MobEffects.LUCK, new Color(51, 153, 0));
            potionColorMap.put(MobEffects.UNLUCK, new Color(192, 164, 77));
        });

    }

    @Override
    public void guiDraw(int mouseX, int mouseY, float partialTicks) {
        super.guiDraw(mouseX, mouseY, partialTicks);
            render();
    }

    @Override
    public void hudDraw(float partialTicks) {
        render();
    }

    @Override
    public void guiUpdate(int mouseX, int mouseY, float partialTicks) {
        super.guiUpdate(mouseX, mouseY, partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public void hudUpdate(float partialTicks) {
        super.hudUpdate(partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(label);
    }

    @Override
    public float getHeight() {
        return (Managers.TEXT.getStringHeight() + textOffset.getValue()) * potCounter;
    }

}
