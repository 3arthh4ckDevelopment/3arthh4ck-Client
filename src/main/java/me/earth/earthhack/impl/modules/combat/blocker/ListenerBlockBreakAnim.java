package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.combat.blocker.util.BlockerDetection;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.BlockPos;

public class ListenerBlockBreakAnim extends ModuleListener<Blocker, PacketEvent.Receive<SPacketBlockBreakAnim>>
{
    public ListenerBlockBreakAnim(final Blocker module) {
        super(module, PacketEvent.Receive.class, SPacketBlockBreakAnim.class);
    }

    public void invoke(final PacketEvent.Receive<SPacketBlockBreakAnim> event) {
        if (!(module.modeCev.getValue() == BlockerDetection.Touch
                || module.modeSides.getValue() == BlockerDetection.Touch))
            return;

        if (mc.world == null || mc.player == null || event.getPacket() == null)
            return;

        if (event.getPacket().getProgress() < module.detectTime.getValue())
            return;

        final BlockPos blockPosition = event.getPacket().getPosition();
        if (mc.world.getBlockState(blockPosition).getBlock() == Blocks.BEDROCK)
            return;

        if (blockPosition.getDistance(
                PlayerUtil.getPlayerPos().getX(),
                PlayerUtil.getPlayerPos().getY(),
                PlayerUtil.getPlayerPos().getZ()) > 6.0f)
            return;

        module.scanAndPlace(blockPosition, !mc.world.isAirBlock(blockPosition));
    }

}