package me.earth.earthhack.impl.modules.render.targethud;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.event.events.render.Render2DEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.combat.autocrystal.AutoCrystal;
import me.earth.earthhack.impl.modules.combat.killaura.KillAura;
import me.earth.earthhack.impl.modules.render.targethud.util.THud;
import me.earth.earthhack.impl.modules.render.targethud.util.TargetType;
import me.earth.earthhack.impl.util.minecraft.PhaseUtil;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import me.earth.earthhack.impl.util.minecraft.PushMode;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

import static me.earth.earthhack.impl.modules.client.hud.HUD.RENDERER;
import static me.earth.earthhack.impl.util.otherplayers.IgnoreSelfClosest.GetClosestIgnore;
import static org.lwjgl.opengl.GL11.glScalef;

public class TargetHud extends Module
{
    protected static final ModuleCache<AutoCrystal> AUTO_CRYSTAL = Caches.getModule(AutoCrystal.class);
    protected static final ModuleCache<KillAura> KILL_AURA = Caches.getModule(KillAura.class);
    public final ColorSetting bgColor =
            register(new ColorSetting("BackGround", new Color(0, 39, 166, 180)));
    public final ColorSetting fColor =
            register(new ColorSetting("TextColor", new Color(230, 230, 230, 220)));
    protected final Setting<THud> style =
            register(new EnumSetting<>("Style", THud.Mode1));
    protected final Setting<TargetType> targeting =
            register(new EnumSetting<>("Targeting", TargetType.None));
    protected final Setting<Integer> maxSetting =
            register(new NumberSetting<>("Detect-Range",10 , 0, 30));
    protected final Setting<Float> posX =
            register(new NumberSetting<>("PosX", 700.0f, 0.0f, (float) Render2DUtil.CSWidth()));
    protected final Setting<Float> posY =
            register(new NumberSetting<>("PosY", 200.0f, 0.0f, (float) Render2DUtil.CSHeight()));
    /*
    protected final Setting<Float> scale =
            register(new NumberSetting<>("Scale", 1.0f, 0.3f, 3.0f));
     */
    protected final Setting<Boolean> pretty =
            register(new BooleanSetting("Pretty", true));
    protected final Setting<Boolean> ping =
            register(new BooleanSetting("Ping", true));
    protected final Setting<Boolean> distance =
            register(new BooleanSetting("Distance", true));
    protected final Setting<Boolean> phase      =
            register(new BooleanSetting("Phase", false));
    protected final Setting<PushMode> pushMode =
            register(new EnumSetting<>("PhasePushDetect", PushMode.None))
                    .setComplexity(Complexity.Expert);


    private static final DecimalFormat df = new DecimalFormat("0.00");

    Vec3i[] safePositions = new Vec3i[]{new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(0, 0, -1)};
    // implement surround in hud with this?

    public TargetHud()
    {
        super("TargetHud", Category.Render);
        this.setData(new TargetHudData(this));
        this.listeners.add(new LambdaListener<>(Render2DEvent.class, e -> {
            if (mc.player == null || mc.world == null) {return;}
            double maxDistance = maxSetting.getValue();
            EntityPlayer closestPlayer = GetClosestIgnore(maxDistance);
            if (mc.player == null || mc.world == null || closestPlayer == null) {return;}

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
            Integer hp = (int) health;
            String name = closestPlayer.getName();
            int nameWidth = Managers.TEXT.getStringWidth(name);

            float x = posX.getValue();
            float y = posY.getValue();

            float endMeasureX, endMeasureY;

            if (style.getValue() == THud.Mode1) {
                endMeasureX = 100;
                endMeasureY = 25;
            } else if (style.getValue() == THud.Mode2) {
                endMeasureX = 180;
                endMeasureY = 90;
            } else if (style.getValue() == THud.Mode3){
                endMeasureX = (nameWidth < 58 ? 58 : nameWidth);
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
                    int blast = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(3), stack);
                    int prot = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack);
                    if (prot != 0) {
                        protVal++;
                    }
                    if (blast != 0) {
                        blastVal++;
                    }
                }
            }

            double protCalc = protVal * 100 / (protVal + blastVal);
            double blastCalc = blastVal * 100 / (protVal + blastVal);

            if (style.getValue() == THud.Mode2) {
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name,x + ((endX - x) / 2 - nameWidth / 2.0f), y + 7, 0xff670067);
                } else {
                    RENDERER.drawString(name,x + ((endX - x)  / 2 - nameWidth / 2.0f), y + 7, fColor.getValue().getRGB());
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
                Render2DUtil.drawPlayerFace(closestPlayer,  xEnd - 5, yEnd, 25, 25);
                if (distance.getValue()) {
                    RENDERER.drawString("Distance: " + df.format(closestPlayer.getDistance(mc.player)), x + 7, y + 70, fColor.getValue().getRGB());
                }

                NetworkPlayerInfo playerInfo = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(closestPlayer.getUniqueID());
                if (playerInfo != null && ping.getValue()) {
                    RENDERER.drawString("Ping: " + playerInfo.getResponseTime() + "ms", x + 7, y + 80, fColor.getValue().getRGB());
                }
                Render2DUtil.progressBar(x + 48, x + 48 + (hp / 36.0F * 80), y + 28, 5, 0x77ff0000);
                renderArmor((int) (x + 80), (int) (y + 65), closestPlayer);
            } else if (style.getValue() == THud.Mode1) {
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                float v = (endX - x + (endY - y)) / 2;
                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name,  x + (v - nameWidth / 2.0f), y + 7, 0xff670067);
                } else {
                    RENDERER.drawString(name, x + (v - nameWidth / 2.0f), y + 7, fColor.getValue().getRGB());
                }
                Render2DUtil.drawPlayerFace(closestPlayer, (int) x, (int) y, (int) (endY - y), (int) (endY - y));
                Render2DUtil.progressBar(x + (10 + (endY - y)), x + (((endY - y) + hp / 36.0F * ( endX - (endY - y) - x)) - 10), y + 19, 3, 0x77ff0000);
            } else if (style.getValue() == THud.Mode3) {
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name,x + ((endX - x) / 2 - nameWidth / 2.0f), y, 0xff670067);
                } else {
                    RENDERER.drawString(name,x + ((endX - x)  / 2 - nameWidth / 2.0f), y, fColor.getValue().getRGB());
                }

                Render2DUtil.progressBar(x + 7, x - 7 + (hp / 36.0F * (Math.max(nameWidth, 58))), endY - 5.0f, 5, 0x77ff0000);
                Render2DUtil.drawPlayer(closestPlayer, 0.8f, x + (float) (Math.max(nameWidth, 58)) / 2, endY - 15.0f);
            } else if (style.getValue() == THud.Mode4) {
                if (!pretty.getValue()) {
                    Render2DUtil.drawRect(x, y, endX, endY, bgColor.getRGB());
                } else {
                    Render2DUtil.roundedRect(x, y, endX, endY, 3, bgColor.getRGB());
                }

                if (phase.getValue() && PhaseUtil.isPhasing(closestPlayer, pushMode.getValue())) {
                    RENDERER.drawString(name,endX - nameWidth - 2, y + 2, 0xff670067);
                } else {
                    RENDERER.drawString(name,endX - nameWidth - 2, y + 2, fColor.getValue().getRGB());
                }

                if (protVal != 0 || blastVal != 0) {
                    RENDERER.drawString("THREAT", x + 2, y + 2, 0xffff0000);
                } else {
                    RENDERER.drawString("NAKED", x + 2, y + 2, 0xff00ff00);
                }

                int safeCounter = 0;
                BlockPos playerPos = closestPlayer.getPosition();
                for (Vec3i vec3i : safePositions) {
                    IBlockState blockState = mc.world.getBlockState(playerPos.add(vec3i));
                    Block block = blockState.getBlock();
                    if ((Block.getIdFromBlock(block) == 49 || Block.getIdFromBlock(block) == 7))
                        safeCounter++;
                }

                if (safeCounter == 4)
                    RENDERER.drawString("SAFE", x + 2, y + 12, 0xff00ff00);
                else
                    RENDERER.drawString("UNSAFE", x + 2, y + 12, 0xffff0000);

                renderArmor((int) x - 17, (int) (y + 19), closestPlayer);

                Render2DUtil.drawPlayerFace(closestPlayer, (int) (endX - 28), (int) (y + Managers.TEXT.getStringHeight() + 2), 22, 22);
                Render2DUtil.progressBar(x + 7.5f, x - 7.5f + (hp / 36.0F * endMeasureX), endY - 2.5f, 5, 0x77ff0000);
                GL11.glPushMatrix();
                glScalef(0.5f, 0.5f, 1.0f);
                RENDERER.drawString(hp.toString(), (x - 7.5f + (hp / 36.0F * endMeasureX)) / 0.5f - Managers.TEXT.getStringWidth(hp.toString()) / 2.0f, (endY - 4.5f) / 0.5f, fColor.getValue().getRGB());
                GL11.glPopMatrix();
            }
        }));
        GlStateManager.resetColor();
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
}

// TODO: totem pops
