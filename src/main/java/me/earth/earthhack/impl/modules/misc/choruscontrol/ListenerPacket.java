package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import java.util.ArrayList;

public class ListenerPacket extends ModuleListener<ChorusControl, PacketEvent> { // idk how to really use all of 3arthh4ck events, not really familiar with the system :D
    public ListenerPacket(ChorusControl module){
        super(module, PacketEvent.class);
    }
    ArrayList<Packet<?>> packets = new ArrayList<>();
    public void invoke(PacketEvent event){
        if((mc.world == null || mc.player == null || !module.startCancel) && module.eatingChorus)
            return;
        Packet<?> packet = event.getPacket();

        // thank you gs++ / TechAle(?)

        if(packet instanceof SPacketPlayerPosLook){
            SPacketPlayerPosLook posLook = (SPacketPlayerPosLook) packet;
            if(module.x != (int) posLook.getX() || module.y != (int) posLook.getY() || module.z != (int) posLook.getZ())
                mc.player.setPosition(posLook.getX(), posLook.getY(), posLook.getZ());
            event.setCancelled(true);
        }else if (event.getPacket() instanceof CPacketPlayer){
            this.packets.add(packet);
            event.setCancelled(true);
        }else if (event.getPacket() instanceof CPacketConfirmTeleport){
            this.packets.add(packet);
            event.setCancelled(true);
        }
    }
}

