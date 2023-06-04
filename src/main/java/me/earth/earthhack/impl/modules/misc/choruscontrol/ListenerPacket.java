package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerPacket extends ModuleListener<ChorusControl, PacketEvent.Send<Packet<?>>> {
    public ListenerPacket(ChorusControl module){
        super(module, PacketEvent.Send.class);
    }

    /*
     * Pasted from GameSense++ by TechAle.
     * Will do my own implementation of this, but later. This has to do for now, as this will apparently be released soon(?)
     * TODO: Rewrite...
     */
    public void invoke(PacketEvent.Send<Packet<?>> e)
    {
        if(mc.player == null) return;
        if(mc.world == null) return;

        if(module.valid)
        {
            Packet<?> sentPacket = e.getPacket();

            if(sentPacket instanceof SPacketPlayerPosLook)
            {
                SPacketPlayerPosLook posLookPacket = (SPacketPlayerPosLook) sentPacket;
                if(module.x != (int) posLookPacket.getX()
                    || module.y != (int) posLookPacket.getY()
                    || module.z != (int) posLookPacket.getZ())
                {
                    module.doTeleport(posLookPacket.getX(), posLookPacket.getY(), posLookPacket.getZ());
                }
                e.setCancelled(true);
            }
            else if(e.getPacket() instanceof CPacketPlayer)
            {
                module.packets.add(sentPacket);
                e.setCancelled(true);
            }
            else if(e.getPacket() instanceof CPacketConfirmTeleport)
            {
                module.packets.add(sentPacket);
                e.setCancelled(true);
            }

        }
    }
}

