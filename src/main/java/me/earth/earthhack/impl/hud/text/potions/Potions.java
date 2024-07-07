package me.earth.earthhack.impl.hud.text.potions;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.hud.DynamicHudElement;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.editor.HudEditor;
import me.earth.earthhack.impl.util.render.ColorUtil;
import me.earth.earthhack.impl.util.render.hud.HudRainbow;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;

//TODO: hud potion symbols
public class Potions extends DynamicHudElement {

    private static final Potions INSTANCE = new Potions();
    private static final ModuleCache<HudEditor> HUD_EDITOR = Caches.getModule(HudEditor.class);

    private final Setting<PotionColor> potionColor =
            register(new EnumSetting<>("PotionColor", PotionColor.Phobos));
    private final Setting<Integer> textOffset =
            register(new NumberSetting<>("Offset", 2, 0, 10));

    private final String label = "[Potions] No effects applied."; // render this?
    private int effCounter = 0;

    protected void onRender() {
        if (mc.player != null) {
            ArrayList<Potion> sorted = new ArrayList<>();
            effCounter = 0;
            for (Potion potion : Potion.REGISTRY) {
                if (potion != null) {
                    if (mc.player.isPotionActive(potion)) {
                        effCounter++;
                        sorted.add(potion);
                    }
                }
            }
            sorted.sort(Comparator.comparingDouble(potion -> -Managers.TEXT.getStringWidth(I18n.format(potion.getName()) + (mc.player.getActivePotionEffect(potion).getAmplifier() > 0 ? " " + (mc.player.getActivePotionEffect(potion).getAmplifier() + 1) : "") + ChatFormatting.GRAY + " " + Potion.getPotionDurationString(Objects.requireNonNull(mc.player.getActivePotionEffect(potion)), 1.0F))));
            int offset = 0;
            float yPos = (directionY() == TextDirectionY.BottomToTop ? getY() + (sorted.size() * (Managers.TEXT.getStringHeight() + textOffset.getValue())) - Managers.TEXT.getStringHeightI() : getY());
            float borderDistance = simpleCalcX(getWidth());
            if (!sorted.isEmpty()) {
                for (Potion potion : sorted) {
                    PotionEffect effect = mc.player.getActivePotionEffect(potion);
                    if (effect != null) {
                        final String label = I18n.format(potion.getName()) + (effect.getAmplifier() > 0 ? " " + (effect.getAmplifier() + 1) : "") + ChatFormatting.GRAY + " " + Potion.getPotionDurationString(effect, 1.0F);
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        float xPos = getX() - simpleCalcX(Managers.TEXT.getStringWidth(label));
                        renderPotionText(label, borderDistance + xPos, yPos + offset * (directionY() == TextDirectionY.BottomToTop ? -1 : 1), effect.getPotion());
                        offset += Managers.TEXT.getStringHeightI() + textOffset.getValue();
                    }
                }
            } else if (isGui()) {
                HudRenderUtil.renderText(label, getX(), getY());
            }
        }
    }

    public void renderPotionText(String text, float x, float y, Potion potion) {
        String colorCode = (potionColor.getValue() == PotionColor.Normal || potionColor.getValue() == PotionColor.Phobos) ? "" : HUD_EDITOR.get().colorMode.getValue().getColor();
        Managers.TEXT.drawStringWithShadow(colorCode + text,
                x,
                y,
                (potionColor.getValue() == PotionColor.Normal || potionColor.getValue() == PotionColor.Phobos) ? potionColorMap.get(potion).getRGB() : (
                        HUD_EDITOR.get().colorMode.getValue() == HudRainbow.None
                                ? HUD_EDITOR.get().color.getValue().getRGB()
                                : (HUD_EDITOR.get().colorMode.getValue() == HudRainbow.Static ? (ColorUtil.staticRainbow((y + 1) * 0.89f, HUD_EDITOR.get().color.getValue())) : 0xffffffff)));
    }

    void normalColors() {
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
        potionColorMap.put(MobEffects.SATURATION, new Color(248, 36, 35));
        potionColorMap.put(MobEffects.GLOWING, new Color(148, 160, 97));
        potionColorMap.put(MobEffects.LEVITATION, new Color(206, 255, 255));
        potionColorMap.put(MobEffects.LUCK, new Color(51, 153, 0));
        potionColorMap.put(MobEffects.UNLUCK, new Color(192, 164, 77));
    }

    private final Map<Potion, Color> potionColorMap = new HashMap<>();

    public Potions() {
        super("PotionEffects", "Displays active potion effects.", HudCategory.Text, 120, 120);

        normalColors();

        this.potionColor.addObserver(e -> {
            System.out.println(potionColorMap.size());
            if (potionColor.getValue() != PotionColor.Normal) {
                normalColors();
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
                potionColorMap.put(MobEffects.SATURATION, new Color(248, 36, 35));
                potionColorMap.put(MobEffects.GLOWING, new Color(148, 160, 97));
                potionColorMap.put(MobEffects.LEVITATION, new Color(206, 255, 255));
                potionColorMap.put(MobEffects.LUCK, new Color(51, 153, 0));
                potionColorMap.put(MobEffects.UNLUCK, new Color(192, 164, 77));
            } else {
                potionColorMap.clear();
            }
        });
    }

    public static Potions getInstance() {
        return INSTANCE;
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(label.trim());
    }

    @Override
    public float getHeight() {
        return (Managers.TEXT.getStringHeight() + textOffset.getValue()) * (effCounter == 0 ? 1 : effCounter);
    }
}