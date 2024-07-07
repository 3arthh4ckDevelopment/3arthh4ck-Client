package me.earth.earthhack.impl.hud.text.coordinates;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
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

    private final Random rng = new Random();
    private String coords = "";
    private Vec3i startingPos = null;
    private Vec3i realPos = null;

    protected void onRender() {
        Vec3i pos = smartCoords();
        long x = pos.getX();
        long y = pos.getY();
        long z = pos.getZ();

        String overworld = String.format(ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s", x, y, z);

        if (dimension.getValue())
            coords = mc.player.dimension == -1
                    ? String.format(ChatFormatting.PREFIX_CODE + "7%s " + ChatFormatting.PREFIX_CODE + "f" + "%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "7%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "7%s " + ChatFormatting.PREFIX_CODE + "f" + "%s", x, surroundWithBrackets(String.valueOf(x * 8)), y, z, surroundWithBrackets(String.valueOf(z * 8)))
                    : (mc.player.dimension == 0 ? String.format(ChatFormatting.PREFIX_CODE + "f%s " + ChatFormatting.PREFIX_CODE + "7" + "%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s" + ChatFormatting.PREFIX_CODE + "8, " + ChatFormatting.PREFIX_CODE + "f%s " + ChatFormatting.PREFIX_CODE + "7" + "%s", x, surroundWithBrackets(String.valueOf(x / 8)), y, z, surroundWithBrackets(String.valueOf(z / 8))) : overworld);
        else
            coords = overworld;

        HudRenderUtil.renderText(coords, getX(), getY());
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

    private Vec3i getRandomVec3i() {
        return new Vec3i(rng.nextInt(), mc.player.posY, rng.nextInt());
    }

    public Coordinates() {
        super("Coordinates", "Displays your coordinates.", HudCategory.Text, 110, 150);
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(coords.trim());
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }
}
