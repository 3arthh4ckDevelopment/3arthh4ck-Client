package me.earth.earthhack.impl.util.otherplayers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import static me.earth.earthhack.api.util.interfaces.Globals.mc;

public class IgnoreSelfClosest {
    public static EntityPlayer GetClosestIgnore (Double maxdist) {
        double closestDistance = Double.MAX_VALUE;
        double calcDistance = Double.MAX_VALUE;
        EntityPlayer closestPlayer = null;
        for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
            if (player.getDistanceSq(Minecraft.getMinecraft().player) < closestDistance) {
                if (player != mc.player) {
                    closestPlayer = player;
                    closestDistance = player.getDistanceSq(Minecraft.getMinecraft().player);
                    calcDistance = player.getDistance(mc.player);
                }
            }
        }

        if (calcDistance <= maxdist) {
            return closestPlayer;
        } else {
            return null;
        }
    }
}
