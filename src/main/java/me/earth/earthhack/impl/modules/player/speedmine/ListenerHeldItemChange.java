package me.earth.earthhack.impl.modules.player.speedmine;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ListenerHeldItemChange extends ModuleListener<Speedmine, PacketEvent.Send<CPacketHeldItemChange>> {

    public ListenerHeldItemChange(Speedmine module) {
        super(module, PacketEvent.Send.class, CPacketHeldItemChange.class);
    }

    @Override
    public void invoke(PacketEvent.Send<CPacketHeldItemChange> event) {
        if(module.resetSwap.getValue()
                && module.getPos() != null
                && mc.world.getBlockState(module.getPos()).getBlock() != Blocks.AIR)
        {
            module.retry();
        }
    }
}
