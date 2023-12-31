package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.combat.blocker.util.BlockerDetection;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;

public class ListenerBlockChange extends ModuleListener<Blocker, PacketEvent.Receive<SPacketBlockChange>>
{
    public ListenerBlockChange(Blocker module)
    {
        super(module, PacketEvent.Receive.class, SPacketBlockChange.class);
    }

    public void invoke(final PacketEvent.Receive<SPacketBlockChange> event) {
        if (!(module.modeCev.getValue() == BlockerDetection.Break
                || module.modeSides.getValue() == BlockerDetection.Break))
            return;

        if (mc.world == null || mc.player == null || event.getPacket() == null)
            return;

        final BlockPos blockPosition = event.getPacket().getBlockPosition();
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
