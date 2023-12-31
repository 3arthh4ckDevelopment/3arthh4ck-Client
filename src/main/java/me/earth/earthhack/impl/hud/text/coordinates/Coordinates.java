package me.earth.earthhack.impl.hud.text.coordinates;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Random;

public class Coordinates extends HudElement {

    private final Setting<Boolean> dimension =
            register(new BooleanSetting("Opposite-Dimension", true));
    private final Setting<Boolean> random =
            register(new BooleanSetting("SmartRandom", false));
    private final Setting<Integer> randomRange =
            register(new NumberSetting<>("RandomRange", 5000, 1000, 50_000));
    private final Setting<Boolean> customBrackets =
            register(new BooleanSetting("CustomBrackets", true));

    private static String coords = "";
    private static Vec3i startingPos = null;
    private static Vec3i realPos = null;
    Random rng = new Random();

    private void render() {
        if (mc.player != null && mc.world != null) {
            final Vec3i pos = smartCoords();
            final long x = pos.getX();
            final long y = pos.getY();
            final long z = pos.getZ();

            String overworld = String.format(ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s", x, y, z);

            if (dimension.getValue())
                coords = mc.player.dimension == -1 ? String.format(ChatFormatting.PREFIX_CODE + "7%s " + ChatFormatting.PREFIX_CODE + "f" + actualBracket()[0] + "%s" + actualBracket()[1] + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "7%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "7%s " + ChatFormatting.PREFIX_CODE + "f" + actualBracket()[0] + "%s" + actualBracket()[1], x, x * 8, y, z, z * 8) : (mc.player.dimension == 0 ? String.format(ChatFormatting.PREFIX_CODE + "f%s " + ChatFormatting.PREFIX_CODE + "7" + actualBracket()[0] + "%s" + actualBracket()[1] + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s " + ChatFormatting.PREFIX_CODE + "7" + actualBracket()[0] + "%s" + actualBracket()[1],
                        x,
                        x / 8,
                        y,
                        z,
                        z / 8) : overworld);
            else
                coords = overworld;
        }
        GlStateManager.pushMatrix();
        HudRenderUtil.renderText(coords, getX(), getY());
        GlStateManager.popMatrix();
    }

    private Vec3i smartCoords() {
        if (MathUtil.distance2D(new Vec3d(mc.player.posX, 0, mc.player.posZ), new Vec3d(0,0,0)) < randomRange.getValue()) {
            return new Vec3i(Math.round(mc.player.posX), Math.round(mc.player.posY), Math.round(mc.player.posZ));
        }
        if (random.getValue()) {
            if (startingPos == null) {
                realPos = new Vec3i(mc.player.posX, mc.player.posY, mc.player.posZ);
                return startingPos = getRandomVec3i();
            } else if (MathUtil.distance2D(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3d(realPos.getX(), realPos.getY(), realPos.getZ())) < 1000) {
                return new Vec3i(startingPos.getX() - (realPos.getX() - Math.round(mc.player.posX)), Math.round(mc.player.posY), startingPos.getZ() + (realPos.getZ() - Math.round(mc.player.posZ)));
            } else {
                realPos = new Vec3i(mc.player.posX, mc.player.posY, mc.player.posZ);
                startingPos = getRandomVec3i();
                return startingPos;
            }
        } else {
            return new Vec3i(Math.round(mc.player.posX), Math.round(mc.player.posY), Math.round(mc.player.posZ));
        }
    }

    private String[] actualBracket() {
        if (customBrackets.getValue())
            return new String[]{ HudRenderUtil.BracketsColor() + HudRenderUtil.Brackets()[0] + HudRenderUtil.BracketsTextColor(), HudRenderUtil.BracketsColor() + HudRenderUtil.Brackets()[1] + TextColor.WHITE };
        else
            return new String[]{ TextColor.GRAY + "[", TextColor.GRAY + "]"};
    }

    private Vec3i getRandomVec3i() {
        return new Vec3i(rng.nextInt(), mc.player.posY, rng.nextInt());
    }

    public Coordinates() {
        super("Coordinates", HudCategory.Text, 110, 150);
        this.setData(new SimpleHudData(this, "Displays your coordinates."));
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
        return Managers.TEXT.getStringWidth(coords);
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }

}
