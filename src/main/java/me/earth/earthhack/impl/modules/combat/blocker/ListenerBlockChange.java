package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.player.reach.Reach;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockAir;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import net.minecraft.network.play.server.SPacketBlockChange;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;

public class ListenerBlockChange extends ModuleListener<Blocker, PacketEvent.Receive<SPacketBlockChange>>
{
    private static final ModuleCache<Reach> REACH = Caches.getModule(Reach.class);
    public ListenerBlockChange(Blocker module)
    {
        super(module, PacketEvent.Receive.class, SPacketBlockChange.class);
    }

    public void invoke(final PacketEvent.Receive<SPacketBlockChange> event) {
        if (mc.world == null || mc.player == null || event.getPacket() == null) {
            return;
        }

        if (event.getPacket().getBlockPosition().getDistance(PlayerUtil.getPlayerPos().getX(), PlayerUtil.getPlayerPos().getY(), PlayerUtil.getPlayerPos().getZ()) > (REACH.isEnabled() ? REACH.get().reach.getValue() : 6.0)) {
            return;
        }

        final BlockPos blockPosition = event.getPacket().getBlockPosition();

        if (module.modeCev.getValue() == Blocker.DetectMode.Breaked) {
            if (module.antiCev.getValue()) {
                BlockPos playerPos = PlayerUtil.getPlayerPos();
                if (blockPosition.equals(playerPos.add(0, 2, 0))) {
                    module.scanAndPlace(playerPos.add(0, 3, 0), false);
                }
            }
        }

        if (module.mode.getValue() == Blocker.DetectMode.Breaked) {
            if (event.getPacket().getBlockState().getBlock() instanceof BlockAir) {
                module.scanAndPlace(blockPosition, true);
            }
        }
    }

}
