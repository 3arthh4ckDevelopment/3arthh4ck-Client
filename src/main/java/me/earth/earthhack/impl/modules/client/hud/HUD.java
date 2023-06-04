package me.earth.earthhack.impl.modules.client.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.*;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.managers.render.TextRenderer;
import me.earth.earthhack.impl.modules.client.hud.arraylist.ArrayEntry;
import me.earth.earthhack.impl.modules.client.hud.modes.HudRainbow;
import me.earth.earthhack.impl.modules.client.hud.modes.Modules;
import me.earth.earthhack.impl.modules.client.hud.modes.PotionColor;
import me.earth.earthhack.impl.modules.client.hud.modes.Potions;
import me.earth.earthhack.impl.util.client.ModuleUtil;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.math.rotation.RotationUtil;
import me.earth.earthhack.impl.util.minecraft.DamageUtil;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.earthhack.impl.util.network.ServerUtil;
import me.earth.earthhack.impl.util.render.ColorHelper;
import me.earth.earthhack.impl.util.render.ColorUtil;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.render.image.NameableImage;
import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import me.earth.earthhack.pingbypass.modules.PbModule;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

// TODO: REWRITE?
public class HUD extends Module {
    public static final TextRenderer RENDERER = Managers.TEXT;

    public final Setting<HudRainbow> colorMode =
            register(new EnumSetting<>("Rainbow", HudRainbow.None));
    public final Setting<Color> color =
            register(new ColorSetting("Color", Color.WHITE));
    protected final Setting<Boolean> logo =
            register(new BooleanSetting("Logo", true));
    protected final Setting<String> logoText =
            register(new StringSetting("LogoText", "3arthh4ck"));
    protected final Setting<Boolean> version =
            register(new BooleanSetting("Version", true));
    protected final Setting<Boolean> coordinates =
            register(new BooleanSetting("Coordinates", true));
    protected final Setting<Boolean> armor =
            register(new BooleanSetting("Armor", true));
    protected final Setting<Boolean> durability =
            register(new BooleanSetting("Durability", false));
    protected final Setting<Boolean> totems =
            register(new BooleanSetting("Totems", false));
    protected final Setting<Integer> totemsXOffset =
            register(new NumberSetting<>("Totems-X-Offset", 0, -10, 10));
    protected final Setting<Integer> totemsYOffset =
            register(new NumberSetting<>("Totems-Y-Offset", 0, -10, 10));
    protected final Setting<Modules> renderModules =
            register(new EnumSetting<>("Modules", Modules.Length));
    protected final Setting<Potions> potions =
            register(new EnumSetting<>("Potions", Potions.Move));
    protected final Setting<PotionColor> potionColor =
            register(new EnumSetting<>("PotionColor", PotionColor.Normal));
    protected final Setting<Boolean> shadow =
            register(new BooleanSetting("Shadow", true));
    protected final Setting<Boolean> ping =
            register(new BooleanSetting("Ping", false));
    protected final Setting<Boolean> speed =
            register(new BooleanSetting("Speed", false));
    protected final Setting<Boolean> fps =
            register(new BooleanSetting("FPS", false));
    protected final Setting<Boolean> tps =
            register(new BooleanSetting("TPS", false));
    protected final Setting<Boolean> currentTps =
            register(new BooleanSetting("CurrentTps", true));
    protected final Setting<Boolean> animations =
            register(new BooleanSetting("Animations", true));
    protected final Setting<Boolean> serverBrand =
            register(new BooleanSetting("ServerBrand", false));
    protected final Setting<Boolean> skeetLine =
            register(new BooleanSetting("SkeetLine", false));
    protected final Setting<Boolean> skeetLineGradient =
            register(new BooleanSetting("SkeetGradient", false));
    protected final Setting<Color> skeetLineColor =
            register(new ColorSetting("SkeetColor", new Color(0x7817ff)));
    protected final Setting<Color> skeetLineColorGradient =
            register(new ColorSetting("SkeetColorGradient", new Color(0x7817ff)));
    protected final Setting<Float> skeetLineWidth =
            register(new NumberSetting<>("SkeetLineWidth", 1.3f, 0.5f, 3.0f));
    //TODO: make an offset from the top
    protected final Setting<Boolean> model =
            register(new BooleanSetting("Model3D", false));
    protected final Setting<Integer> modelX =
            register(new NumberSetting<>("Model-X", 100, 0, Render2DUtil.CSWidth()));
    protected final Setting<Integer> modelY =
            register(new NumberSetting<>("Model-Y", 100, 0, Render2DUtil.CSHeight()));
    protected final Setting<Float> modelScale =
            register(new NumberSetting<>("ModelScale", 0.8f, 0.4f, 2.0f));

    protected final Setting<Boolean> time =
        register(new BooleanSetting("Time", false));
    protected final Setting<String> timeFormat =
        register(new StringSetting("TimeFormat", "hh:mm:ss"));

    protected final Setting<Integer> textOffset =
            register(new NumberSetting<>("Offset", 2, 0, 10))
                .setComplexity(Complexity.Expert);
    protected final Setting<Boolean> image =
            register(new BooleanSetting("Image", false));
    protected final Setting<NameableImage> imageName   =
            register(new ListSetting<>("ImgName", Managers.FILES.getInitialImage(), Managers.FILES.getImages())).setComplexity(Complexity.Dev);
    protected final Setting<Float> imageScale =
            register(new NumberSetting<>("ImgScale", 1.0f, 0.0f, 30.0f)).setComplexity(Complexity.Dev);
    protected final Setting<Float> imageX =
            register(new NumberSetting<>("ImgX", 0.5f,0.0f,1.0f)).setComplexity(Complexity.Dev);
    protected final Setting<Float> imageY =
            register(new NumberSetting<>("ImgY", 0.5f,0.0f,1.0f)).setComplexity(Complexity.Dev);
    protected final Setting<Boolean> reloadImages =
            register(new BooleanSetting("RealoadImages", false)).setComplexity(Complexity.Dev);

    protected final List<Map.Entry<String, Module>> modules = new ArrayList<>();

    protected final Map<Module, ArrayEntry> arrayEntries = new HashMap<>();
    protected final Map<Module, ArrayEntry> removeEntries = new HashMap<>();

    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
    protected ScaledResolution resolution = new ScaledResolution(mc);
    protected int width;
    protected int height;
    protected float animationY = 0;
    private final Map<Potion, Color> potionColorMap = new HashMap<>();

    public HUD() {
        super("HUD", Category.Client);
        this.listeners.add(new ListenerRender(this));
        this.listeners.add(new ListenerPostKey(this));
        this.setData(new HUDData(this));
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
        timeFormat.addObserver(e -> {
            if (!e.isCancelled()) {
                try {
                    formatter = DateTimeFormatter.ofPattern(e.getValue());
                } catch (IllegalArgumentException iae) {
                    ChatUtil.sendMessageScheduled(TextColor.RED + "Invalid DateTimeFormat: " + TextColor.WHITE + e.getValue());
                    formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                }
            }
        });

    }

    public int getArmorY() {
        int y;
        if (mc.player.isInsideOfMaterial(Material.WATER)
                && mc.player.getAir() > 0
                && !mc.player.capabilities.isCreativeMode) {
            y = 65;
        } else if (mc.player.getRidingEntity() != null
                && !mc.player.capabilities.isCreativeMode) {
            if (mc.player.getRidingEntity()
                    instanceof EntityLivingBase) {
                EntityLivingBase entity =
                        (EntityLivingBase) mc.player.getRidingEntity();
                y = (int) (45
                        + Math.ceil((entity.getMaxHealth()
                        - 1.0F)
                        / 20.0F)
                        * 10);
            } else {
                y = 45;
            }
        } else if (mc.player.capabilities.isCreativeMode) {
            y = mc.player.isRidingHorse() ? 45 : 38;
        } else {
            y = 55;
        }
        return y;
    }

    protected void renderLogo() {
        if (logo.getValue()) {
            if (version.getValue()) {
                renderText(logoText.getValue() + " - " + Earthhack.VERSION, 2, 2);
            } else {
                renderText(logoText.getValue(), 2, 2);
            }
        }
    }

    protected void skeetLine() {
        if (skeetLine.getValue()) {
            if (skeetLineGradient.getValue()) {
                Render2DUtil.drawGradientRect(0, 0, Render2DUtil.CSWidth(), skeetLineWidth.getValue(), true, skeetLineColor.getValue().getRGB(), skeetLineColorGradient.getValue().getRGB());
            } else {
                Render2DUtil.drawRect(0, 0, Render2DUtil.CSWidth(), skeetLineWidth.getValue(), skeetLineColor.getValue().getRGB());
            }
        }
    }

    protected void renderImage() {
        if (image.getValue() && imageName.getValue().getTexture() != null) {
            float x = imageX.getValue() * Render2DUtil.CSWidth();
            float y = imageY.getValue() * Render2DUtil.CSHeight();
            GL11.glPushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("earthhack/images" + imageName.getValue().getTexture().getClass().getName()));
            Gui.drawScaledCustomSizeModalRect(30, 30, 0, 0, 30, 30, 30, 30, 30, 30);
            GL11.glPopMatrix();
        }
    }
    //TODO: fix the rendering

    protected void renderModules() {
        int offset = 0;
        EntityPlayerSP player;
        if (serverBrand.getValue() && (player = mc.player) != null) {
            String serverBrand = "ServerBrand " + TextColor.GRAY + player.getServerBrand();
            renderText(serverBrand, width - 2 - RENDERER.getStringWidth(serverBrand), height - 2 - RENDERER.getStringHeightI() - offset - animationY);
            offset += RENDERER.getStringHeightI() + textOffset.getValue();
        }

        if (potions.getValue() == Potions.Text) {
            final ArrayList<Potion> sorted = new ArrayList<>();
            for (final Potion potion : Potion.REGISTRY) {
                if (potion != null) {
                    if (mc.player.isPotionActive(potion)) {
                        sorted.add(potion);
                    }
                }
            }
            sorted.sort(Comparator.comparingDouble(potion -> -RENDERER.getStringWidth(I18n.format(potion.getName()) + (mc.player.getActivePotionEffect(potion).getAmplifier() > 0 ? " " + (mc.player.getActivePotionEffect(potion).getAmplifier() + 1) : "") + ChatFormatting.GRAY + " " + Potion.getPotionDurationString(Objects.requireNonNull(mc.player.getActivePotionEffect(potion)), 1.0F))));
            for (final Potion potion : sorted) {
                final PotionEffect effect = mc.player.getActivePotionEffect(potion);
                if (effect != null) {
                    final String label = I18n.format(potion.getName()) + (effect.getAmplifier() > 0 ? " " + (effect.getAmplifier() + 1) : "") + ChatFormatting.GRAY + " " + Potion.getPotionDurationString(effect, 1.0F);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    final int x = width - 2 - RENDERER.getStringWidth(label);
                    renderPotionText(label, x, height - 2 - RENDERER.getStringHeightI() - offset - animationY, effect.getPotion());
                    offset += RENDERER.getStringHeightI() + textOffset.getValue();
                }
            }
        }

        if (speed.getValue()) {
            String text = "Speed " + TextColor.GRAY + MathUtil.round(Managers.SPEED.getSpeed(), 2) + " km/h";
            renderText(text, width - 2 - RENDERER.getStringWidth(text), height - 2 - RENDERER.getStringHeightI() - offset - animationY);
            offset += RENDERER.getStringHeightI() + textOffset.getValue();
        }

        if (tps.getValue()) {
            String tps = "TPS " + TextColor.GRAY + MathUtil.round(Managers.TPS.getTps(), 2);
            if (currentTps.getValue())
            {
                tps += TextColor.WHITE + " [" + TextColor.GRAY + MathUtil.round(Managers.TPS.getCurrentTps(), 2) + TextColor.WHITE + "]";
            }

            renderText(tps, width - 2 - RENDERER.getStringWidth(tps), height - 2 - RENDERER.getStringHeightI() - offset - animationY);
            offset += RENDERER.getStringHeightI() + textOffset.getValue();
        }

        if (time.getValue()) {
            LocalDateTime time = LocalDateTime.now();
            String text;
            try {
                text = "Time " + TextColor.GRAY + formatter.format(time);
            } catch (DateTimeException e) {
                ModuleUtil.sendMessageWithAquaModule(this, TextColor.RED + "Can not render time: " + e.getMessage(), "time");
                text = "Time " + TextColor.GRAY + TextColor.RED + "Invalid";
            }

            renderText(text, width - 2 - RENDERER.getStringWidth(text), height - 2 - RENDERER.getStringHeightI() - offset - animationY);
            offset += RENDERER.getStringHeightI() + textOffset.getValue();
        }

        if (fps.getValue()) {
            String fps = "FPS " + TextColor.GRAY + Minecraft.getDebugFPS();
            renderText(fps, width - 2 - RENDERER.getStringWidth(fps), height - 2 - RENDERER.getStringHeightI() - offset - animationY);
            offset += RENDERER.getStringHeightI() + textOffset.getValue();
        }

        if (ping.getValue()) {
            String ping = "Ping " + TextColor.GRAY + ServerUtil.getPing();
            renderText(ping, width - 2 - RENDERER.getStringWidth(ping), height - 2 - RENDERER.getStringHeightI() - offset - animationY);
        }

        if (coordinates.getValue()) {
            final long x = Math.round(mc.player.posX);
            final long y = Math.round(mc.player.posY);
            final long z = Math.round(mc.player.posZ);
            final String coords = mc.player.dimension == -1 ? String.format(ChatFormatting.PREFIX_CODE + "7%s " + ChatFormatting.PREFIX_CODE + "f[%s]" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "7%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "7%s " + ChatFormatting.PREFIX_CODE + "f[%s]", x, x * 8, y, z, z * 8) : (mc.player.dimension == 0 ? String.format(ChatFormatting.PREFIX_CODE + "f%s " + ChatFormatting.PREFIX_CODE + "7[%s]" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s " + ChatFormatting.PREFIX_CODE + "7[%s]",
                    x,
                    x / 8,
                    y,
                    z,
                    z / 8) : String.format(ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s", x, y, z));
            renderText(coords, 2, height - 2 - RENDERER.getStringHeightI() - animationY);
            final String dir = RotationUtil.getDirection4D(false);
            renderText(dir, 2, height - 3 - RENDERER.getStringHeightI() * 2 - animationY);
        }

        if (totems.getValue()) {
            RenderItem itemRender = mc.getRenderItem();
            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            int totems = InventoryUtil.getCount(Items.TOTEM_OF_UNDYING);

            if (totems > 0) {
                int x = width / 2 - (totemsXOffset.getValue()) - 7;
                int y = height - (totemsYOffset.getValue()) - getArmorY();
                itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(mc.player, new ItemStack(Items.TOTEM_OF_UNDYING), x, y);
                itemRender.zLevel = 0.0f;
                GlStateManager.disableDepth();
                String text = String.valueOf(totems);
                renderText(text, x + 17 - RENDERER.getStringWidth(text), y + 9);
                GlStateManager.enableDepth();
            }
        }

        if (model.getValue()) {
            Render2DUtil.drawPlayer(mc.player, modelScale.getValue(), modelX.getValue(), modelY.getValue());
        }
        renderArmor();

        if (renderModules.getValue() != Modules.None) {
            boolean move = potions.getValue() == Potions.Move && !mc.player.getActivePotionEffects().isEmpty();
            int j = move ? 2 : 0;
            int o = move ? 5 : 2;
            int moduleOffset = Managers.TEXT.getStringHeightI() + textOffset.getValue();
            if (animations.getValue()) {
                for (Map.Entry<String, Module> module : modules) {
                    if (isArrayMember(module.getValue()))
                        continue;
                    getArrayEntries().put(module.getValue(), new ArrayEntry(module.getValue()));
                    if (!(module.getValue() instanceof PbModule)) {
                        getArrayEntries()
                            .entrySet()
                            .removeIf(m -> m.getKey() instanceof PbModule
                                && Objects.equals(
                                    ((PbModule) m.getKey()).getModule(),
                                    module.getValue()));
                    }
                }

                Map<Module, ArrayEntry> arrayEntriesSorted;
                if (renderModules.getValue() == Modules.Length) {
                    arrayEntriesSorted = getArrayEntries().entrySet().stream().sorted(Comparator.comparingDouble(entry -> Managers.TEXT.getStringWidth(ModuleUtil.getHudName(entry.getKey())) * -1)).collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
                } else {
                    arrayEntriesSorted = getArrayEntries().entrySet().stream().sorted(Comparator.comparing(entry -> ModuleUtil.getHudName(entry.getKey()))).collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
                }
                for (ArrayEntry arrayEntry : arrayEntriesSorted.values()) {
                    arrayEntry.drawArrayEntry(width - 2, o + j * moduleOffset);
                    j++;
                }
                getRemoveEntries().forEach((key, value) -> getArrayEntries().remove(key));
                getRemoveEntries().clear();
            } else {
                for (Map.Entry<String, Module> module : modules) {
                    renderText(module.getKey(), width - 2 - RENDERER.getStringWidth(module.getKey()), o + j * moduleOffset);
                    j++;
                }
            }
        }
    }

    private void renderArmor() {
        if (armor.getValue()) {
            GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
            int x = 15;
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 3; i >= 0; i--) {
                ItemStack stack = mc.player.inventory.armorInventory.get(i);
                if (!stack.isEmpty()) {
                    int y = getArmorY();
                    final float percent = DamageUtil.getPercent(stack) / 100.0f;
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.625F, 0.625F, 0.625F);
                    GlStateManager.disableDepth();
                    Managers.TEXT.drawStringWithShadow(
                            ((int) (percent * 100.0f)) + "%", (((width >> 1) + x + 1) * 1.6F), (height - y - 3) * 1.6F, ColorHelper.toColor(percent * 120.0f, 100.0f, 50.0f, 1.0f).getRGB());
                    GlStateManager.enableDepth();
                    GlStateManager.scale(1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    mc.getRenderItem()
                            .renderItemIntoGUI(stack,
                                    width / 2 + x,
                                    height - y);
                    mc.getRenderItem()
                            .renderItemOverlays(mc.fontRenderer,
                                    stack,
                                    width / 2 + x,
                                    height - y);
                    GlStateManager.popMatrix();
                    x += 18;
                }
            }

            RenderHelper.disableStandardItemLighting();
        }
    }

    public void renderText(String text, float x, float y) {
        String colorCode = colorMode.getValue().getColor();
        RENDERER.drawStringWithShadow(colorCode + text,
                x,
                y,
                colorMode.getValue() == HudRainbow.None
                        ? color.getValue().getRGB()
                        : (colorMode.getValue() == HudRainbow.Static ? (ColorUtil.staticRainbow((y + 1) * 0.89f, color.getValue())) : 0xffffffff));
    }

    public void renderPotionText(String text, float x, float y, Potion potion) {
        String colorCode = potionColor.getValue() == PotionColor.Normal ? "" : colorMode.getValue().getColor();
        RENDERER.drawStringWithShadow(colorCode + text,
                x,
                y,
                potionColor.getValue() == PotionColor.Normal ? potionColorMap.get(potion).getRGB() : (
                        colorMode.getValue() == HudRainbow.None
                                ? color.getValue().getRGB()
                                : (colorMode.getValue() == HudRainbow.Static ? (ColorUtil.staticRainbow((y + 1) * 0.89f, color.getValue())) : 0xffffffff)));
    }


    public Map<Module, ArrayEntry> getArrayEntries() {
        return arrayEntries;
    }

    public Map<Module, ArrayEntry> getRemoveEntries() {
        return removeEntries;
    }

    protected boolean isArrayMember(Module module) {
        return getArrayEntries().containsKey(module)
            || module instanceof PbModule
               && getArrayEntries().containsKey(((PbModule) module)
                                                    .getModule());
    }

}
