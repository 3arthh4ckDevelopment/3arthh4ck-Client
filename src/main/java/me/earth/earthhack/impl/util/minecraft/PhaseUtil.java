package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.interfaces.Globals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Should detect the players that packetfly slightly into a block to get less
 * damage.
 */
public class PhaseUtil implements Globals {
    // TODO: we could also use pushOutOfBlocks??? quite easy to bypass this --> minecraft:_/_ (this needs a fix)
    public static boolean isPhasing(EntityPlayer entity, PushMode mode) {
        BlockPos entityPos = entity.getPosition();
        BlockPos blockPos = entityPos.add(0, 0, 0);
        float blockHardness = mc.world.getBlockState(blockPos).getBlock().blockHardness;
        if (blockHardness < 0.1) {
            return false;
        }

        if (mode == PushMode.None) {
            Vec3d pos = new Vec3d(
                    entity.serverPosX / 4096.0,
                    entity.serverPosY / 4096.0,
                    entity.serverPosZ / 4096.0);
            float width = entity.width / 2.0f;
            AxisAlignedBB bb = new AxisAlignedBB(
                pos.x - width,
                pos.y,
                pos.z - width,
                pos.x + width,
                pos.y + entity.height,
                pos.z + width
            );

            // density on slabs is lower 1.0 lol
            return DamageUtil.getBlockDensity(pos, bb, mc.world, false, false, false, false) < 1.0f;
        }

        MotionTracker tracker = new MotionTracker(mc.world, entity);
        tracker.resetMotion();
        tracker.shrinkPush = true;
        tracker.pushOutOfBlocks(mode);
        return tracker.motionX != 0.0
            || tracker.motionY != 0.0
            || tracker.motionZ != 0.0;
    }

}
