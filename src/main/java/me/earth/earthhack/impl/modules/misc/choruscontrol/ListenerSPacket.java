package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.render.entity.StaticModelPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerSPacket extends ModuleListener<ChorusControl, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerSPacket(ChorusControl module){
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    public void invoke(PacketEvent.Receive<SPacketPlayerPosLook> event)
    {
        SPacketPlayerPosLook packet = event.getPacket();
        module.tpX = packet.getX();
        module.tpY = packet.getY();
        module.tpZ = packet.getZ();

        event.setCancelled(true);
        module.time = System.currentTimeMillis();
        module.cancelled = true;
        if(!module.cached){
            module.model = new StaticModelPlayer(mc.player, mc.player.getSkinType().contains("slim"), 0);
            module.cached = true;
        }

        if(module.autoOffDelay.getValue() > 0 && module.autoOffTimer.passed(module.autoOffDelay.getValue()))
            module.disable();
    }
}

