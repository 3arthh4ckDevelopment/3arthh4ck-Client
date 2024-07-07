package me.earth.earthhack.impl.hud.visual.targethud;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.managers.render.TextRenderer;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.combat.autocrystal.AutoCrystal;
import me.earth.earthhack.impl.modules.combat.killaura.KillAura;
import me.earth.earthhack.impl.util.minecraft.PhaseUtil;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import me.earth.earthhack.impl.util.minecraft.PushMode;
import me.earth.earthhack.impl.util.otherplayers.IgnoreSelfClosest;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

public class TargetHud extends HudElement {

    private static final ModuleCache<AutoCrystal> AUTO_CRYSTAL = Caches.getModule(AutoCrystal.class);
    private static final ModuleCache<KillAura> KILL_AURA = Caches.getModule(KillAura.class);
    public final ColorSetting bgColor =
            register(new ColorSetting("BackGround", new Color(0, 39, 166, 180)));
    private final ColorSetting fColor =
            register(new ColorSetting("TextColor", new Color(230, 230, 230, 220)));
    private final Setting<THud> style =
            register(new EnumSetting<>("Style", THud.Mode1));
    private final Setting<TargetType> targeting =
            register(new EnumSetting<>("Targeting", TargetType.None));
    private final Setting<Integer> maxSetting =
            register(new NumberSetting<>("Detect-Range",10 , 0, 30));
    private final Setting<Boolean> pretty =
            register(new BooleanSetting("Pretty", true));
    private final Setting<Boolean> ping =
            register(new BooleanSetting("Ping", true));
    private final Setting<Boolean> distance =
            register(new BooleanSetting("Distance", true));
    private final Setting<Boolean> phase =
            register(new BooleanSetting("Phase", false));
    private final Setting<PushMode> pushMode =
            register(new EnumSetting<>("PhasePushDetect", PushMode.None))
                    .setComplexity(Complexity.Expert);

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final TextRenderer RENDERER = Managers.TEXT;
    private float endMeasureX, endMeasureY;

    protected void onRender() {
        EntityPlayer closestPlayer = IgnoreSelfClosest.GetClosestIgnore((double) maxSetting.getValue());
        if (closestPlayer == null) return;

        if (targeting.getValue() == TargetType.CrystalAura) {
            if (AUTO_CRYSTAL.get().getTarget() != null)
                closestPlayer = AUTO_CRYSTAL.get().getTarget();
            else
                return;
        } else if (targeting.getValue() == TargetType.KillAura) {
            if (KILL_AURA.get().getTarget() != null)
                closestPlayer = (EntityPlayer) KILL_AURA.get().getTarget();
            else
                return;
        }

        float health = closestPlayer.getHealth() + closestPlayer.getAbsorptionAmount();
        int hp = (int) health;
        String name = closestPlayer.getName();
        int nameWidth = Managers.TEXT.getStringWidth(name);

        float x = getX();
        float y = getY();

        if (style.getValue() == THud.Mode1) {
            endMeasureX = 100;
            endMeasureY = 25;
        } else if (style.getValue() == THud.Mode2) {
            endMeasureX = 180;
            endMeasureY = 90;
        } else if (style.getValue() == THud.Mode3) {
            endMeasureX = (Math.max(nameWidth, 58));
            endMeasureY = 100;
        } else {
            endMeasureX = 110;
            endMeasureY = 40;
        }

        float endX = x + endMeasureX;
        float endY = y + endMeasureY;


        double protVal = 0;
        double blastVal = 0;

        NonNullList<ItemStack> armor = closestPlayer.inventory.armorInventory;
        for (ItemStack stack : armor) {
            if (stack != null && !stack.isEmpty()) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack) != 0) {
                    protVal++;
                }
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(3), stack) != 0) {
                    blastVal++;
                }
            }
        }

        double protCalc = protVal * 100 / (protVal + blastVal);
        double blastCalc = blastVal * 100 / (protVal + blastVal);

        final float nameX = x + ((endX - x) / 2 - nameWidth / 2.0f);
        switch (style.getValue())
        {
            case Mode1:
                if (name.length() > 14)
                    endX += Managers.TEXT.getStringWidth("a") * name.length() - 14;
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                float v = (endX - x + (endY - y)) / 2;
                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name, x + (v - nameWidth / 2.0f), y + 7, 0xff670067);
                } else {
                    RENDERER.drawString(name, x + (v - nameWidth / 2.0f), y + 7, fColor.getValue().getRGB());
                }
                Render2DUtil.drawPlayerFace(closestPlayer, (int) x, (int) y, (int) (endY - y), (int) (endY - y));
                Render2DUtil.progressBar(x + (10 + (endY - y)), x + (((endY - y) + hp / 36.0F * (endX - (endY - y) - x)) - 10), y + 19, 3, 0x77ff0000);
                break;

            case Mode2:
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name, nameX, y + 7, 0xff670067);
                } else {
                    RENDERER.drawString(name, nameX, y + 7, fColor.getValue().getRGB());
                }
                RENDERER.drawString("HP: " + hp, x + 7, y + 25, fColor.getValue().getRGB());

                if (!Double.isNaN(protCalc)) {
                    RENDERER.drawString("Protection: " + (int) protCalc + "%", x + 7, y + 43, fColor.getValue().getRGB());
                } else {
                    RENDERER.drawString("Protection: " + 0 + "%", x + 7, y + 43, fColor.getValue().getRGB());
                }
                if (!Double.isNaN(blastCalc)) {
                    RENDERER.drawString("Blast: " + (int) blastCalc + "%", x + 7, y + 53, fColor.getValue().getRGB());
                } else {
                    RENDERER.drawString("Blast: " + 0 + "%", x + 7, y + 53, fColor.getValue().getRGB());
                }

                int xEnd = (int) (x + 160);
                int yEnd = (int) (y);
                Render2DUtil.drawPlayerFace(closestPlayer, xEnd - 5, yEnd, 25, 25);
                if (distance.getValue()) {
                    RENDERER.drawString("Distance: " + df.format(closestPlayer.getDistance(mc.player)), x + 7, y + 70, fColor.getValue().getRGB());
                }

                NetworkPlayerInfo playerInfo = mc.getConnection().getPlayerInfo(closestPlayer.entityId > 0 ? closestPlayer.getUniqueID() : mc.player.getUniqueID());
                if (ping.getValue()) {
                    RENDERER.drawString("Ping: " + playerInfo.getResponseTime() + "ms", x + 7, y + 80, fColor.getValue().getRGB());
                }
                Render2DUtil.progressBar(x + 48, x + 48 + (hp / 36.0F * 80), y + 28, 5, 0x77ff0000);
                renderArmor((int) (x + 80), (int) (y + 65), closestPlayer);
                break;

            case Mode3:
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name, nameX, y, 0xff670067);
                } else {
                    RENDERER.drawString(name, nameX, y, fColor.getValue().getRGB());
                }

                Render2DUtil.progressBar(x + 7, x - 7 + (hp / 36.0F * (Math.max(nameWidth, 58))), endY - 5.0f, 5, 0x77ff0000);
                Render2DUtil.drawPlayer(closestPlayer, 30, (int) (x + Math.max(nameWidth, 58) / 2), (int) (endY - 15.0f));
                break;

            case Mode4:
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name, endX - nameWidth - 2, y + 1, 0xff670067);
                } else {
                    RENDERER.drawString(name, endX - nameWidth - 2, y + 1, fColor.getValue().getRGB());
                }

                if (protVal != 0 || blastVal != 0) {
                    RENDERER.drawString("THREAT", x + 2, y + 2, 0xffff0000);
                } else {
                    RENDERER.drawString("NAKED", x + 2, y + 2, 0xff00ff00);
                }

                if (PlayerUtil.isInHole(closestPlayer))
                    RENDERER.drawString("SAFE", x + 2, y + 12, 0xff00ff00);
                else
                    RENDERER.drawString("UNSAFE", x + 2, y + 12, 0xffff0000);

                renderArmor((int) x - 17, (int) (y + 19), closestPlayer);

                Render2DUtil.drawPlayerFace(closestPlayer, (int) (endX - 28), (int) (y + Managers.TEXT.getStringHeight() + 2), 22, 22);
                float endX1 = x - 7.5f + (hp / 36.0F * endMeasureX);
                Render2DUtil.progressBar(x + 7.5f, endX1, endY - 2.5f, 5, 0x77ff0000);
                GL11.glScalef(0.5f, 0.5f, 1.0f);
                break;

            default:
                RENDERER.drawString("TargetHUD", getX(), getY(), 0xffffffff);
                break;
        }
    }

    private void renderArmor(int x, int y, EntityPlayer player) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = player.inventory.armorInventory.get(i);
            if (!stack.isEmpty()) {
                mc.getRenderItem().renderItemIntoGUI(stack, 47 / 2 + x, y);
                mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, 47 / 2 + x, y);
                x += 18;
            }
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public TargetHud() {
        super("TargetHUD", "Displays the player your combat modules are currently targeting.", HudCategory.Visual, 170, 270);
    }

    @Override
    public float getWidth() {
        return endMeasureX == 0 ? Managers.TEXT.getStringWidth("Target Hud") : endMeasureX;
    }

    @Override
    public float getHeight() {
        return endMeasureY == 0 ? Managers.TEXT.getStringHeight() : endMeasureY;
    }

    private enum TargetType {
        CrystalAura,
        KillAura,
        None
    }

    private enum THud {
        Mode1,
        Mode2,
        Mode3,
        Mode4
    }
}
