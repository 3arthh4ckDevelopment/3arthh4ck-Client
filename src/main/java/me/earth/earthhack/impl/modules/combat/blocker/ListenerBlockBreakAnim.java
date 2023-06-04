package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.player.reach.Reach;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.BlockPos;

public class ListenerBlockBreakAnim extends ModuleListener<Blocker, PacketEvent.Receive<SPacketBlockBreakAnim>>
{
    private static final ModuleCache<Reach> REACH = Caches.getModule(Reach.class);
    public ListenerBlockBreakAnim(final Blocker module) {
        super(module, PacketEvent.Receive.class, SPacketBlockBreakAnim.class);
    }

    public void invoke(final PacketEvent.Receive<SPacketBlockBreakAnim> event) {
        if (mc.world == null || mc.player == null || event.getPacket() == null) {
            return;
        }

        if (event.getPacket().getPosition().getDistance(PlayerUtil.getPlayerPos().getX(), PlayerUtil.getPlayerPos().getY(), PlayerUtil.getPlayerPos().getZ()) > (REACH.isEnabled() ? REACH.get().reach.getValue() : 6.0)) {
            return;
        }

        final BlockPos blockPosition = event.getPacket().getPosition();
        if (module.mode.getValue() == Blocker.DetectMode.Touched) {
            if (module.antiCev.getValue()) {
                BlockPos playerPos = PlayerUtil.getPlayerPos();
                if (blockPosition.equals(playerPos.add(0, 2, 0))) {
                    module.scanAndPlace(playerPos.add(0, 3, 0), false);
                }
            }
        }

        if (module.mode.getValue() != Blocker.DetectMode.Touched) {
            if (!mc.world.isAirBlock(blockPosition)) {
                module.scanAndPlace(blockPosition, true);
            }
        }
    }

}
