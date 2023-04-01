package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;

import net.minecraft.network.play.server.SPacketPlayerPosLook;


public class ListenerSPacket extends ModuleListener<ChorusControl, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerSPacket(ChorusControl module){
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    public void invoke(PacketEvent.Receive<SPacketPlayerPosLook> e)
    {
        // Thanks for the clarification to 3arthqu4ke, couldn't have figured this out by myself atleast that fast :D
        SPacketPlayerPosLook poslook = new SPacketPlayerPosLook();

        if(module.valid
                && poslook.getX() != module.x
                && poslook.getY() != module.y
                && poslook.getZ() != module.z)
        {
            e.setCancelled(true); // This is such packet-tinkery magic I don't understand it - this will be done soon though.
            module.cancelled = true;
        }
    }
}

