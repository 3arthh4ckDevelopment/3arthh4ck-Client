package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;


public class ListenerSPacket extends ModuleListener<ChorusControl, PacketEvent.Receive<?>> { // idk how to really use all of 3arthh4ck events, not really familiar with the system :D
    public ListenerSPacket(ChorusControl module){
        super(module, PacketEvent.Receive.class);
    }

    public void invoke(PacketEvent.Receive<?> e)
    {
        if(e.getPacket() instanceof SPacketPlayerPosLook){
            if(module.valid){
                e.setCancelled(true);
            } // WIP!!!
        }
    }
}

