package me.earth.earthhack.impl.util.otherplayers;

import me.earth.earthhack.api.util.interfaces.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class IgnoreSelfClosest implements Globals {
    public static EntityPlayer GetClosestIgnore(Double maxdist) {
        double closestDistance = Double.MAX_VALUE;
        double calcDistance = Double.MAX_VALUE;
        EntityPlayer closestPlayer = null;
        for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
            if (player.getDistanceSq(mc.player) < closestDistance) {
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
