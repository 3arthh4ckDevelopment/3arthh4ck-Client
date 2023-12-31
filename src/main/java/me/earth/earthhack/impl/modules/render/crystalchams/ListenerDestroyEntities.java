package me.earth.earthhack.impl.modules.render.crystalchams;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.network.play.server.SPacketDestroyEntities;

final class ListenerDestroyEntities extends ModuleListener<CrystalChams, PacketEvent.Receive<SPacketDestroyEntities>> {
    public ListenerDestroyEntities(CrystalChams module)
    {
        super(module, PacketEvent.Receive.class, SPacketDestroyEntities.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketDestroyEntities> event)
    {
        mc.addScheduledTask(() ->
        {
            for (int id : event.getPacket().getEntityIDs()) {
                module.scaleMap.remove(id);
            }
        });
    }

}
