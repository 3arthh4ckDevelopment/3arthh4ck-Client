package me.earth.earthhack.impl.modules.misc.chat;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.network.play.client.CPacketChatMessage;

import static me.earth.earthhack.impl.modules.client.commands.Commands.getPrefix;

public class ListenerChatMessage extends ModuleListener<Chat, PacketEvent.Send<CPacketChatMessage>> {
    public ListenerChatMessage(Chat module){
        super(module, PacketEvent.Send.class);
    }

    @Override
    public void invoke(PacketEvent.Send<CPacketChatMessage> e){

        if(mc.player == null) return;
        if(mc.world == null) return;

        if(module.suffix.getValue())
        {
            if (e.getPacket() instanceof CPacketChatMessage){
                String message = e.getPacket().getMessage();

                if (!message.startsWith("/") && !message.startsWith(getPrefix())
                        && !message.endsWith(Chat.EARTH)
                        && !message.endsWith(Chat.CUTEEARTH)
                        && !message.endsWith(Chat.PHOBOS)
                        && !message.endsWith(Chat.RUSHER)
                        && !message.endsWith(module.customSuffix.getValue())
                        && !message.endsWith(Chat.FUTURE)
                        && !message.endsWith(Chat.KONAS)
                        && !message.endsWith(Chat.GAMESENSE)
                        && !message.endsWith(Chat.KAMIBLUE))
                {
                    e.setCancelled(true);
                    mc.player.connection.sendPacket(new CPacketChatMessage(appendSuffix(message)));
                }else if(module.suffixWhispers.getValue() && !message.startsWith(getPrefix())
                        && !message.startsWith(getPrefix())
                        && !message.endsWith(Chat.EARTH)
                        && !message.endsWith(Chat.CUTEEARTH)
                        && !message.endsWith(Chat.PHOBOS)
                        && !message.endsWith(Chat.RUSHER)
                        && !message.endsWith(module.customSuffix.getValue())
                        && !message.endsWith(Chat.FUTURE)
                        && !message.endsWith(Chat.KONAS)
                        && !message.endsWith(Chat.GAMESENSE)
                        && !message.endsWith(Chat.KAMIBLUE)) {
                    if (message.startsWith("/msg") || message.startsWith("/r") || message.startsWith("/w") || message.startsWith("/tell")) {
                        e.setCancelled(true);
                        mc.player.connection.sendPacket(new CPacketChatMessage(appendSuffix(message)));
                    }
                }
            }
        }
    }

    public String appendSuffix(String message)
    {
        switch(module.mode.getValue())
        {
            case Earth:
                return message  + " | " + Chat.EARTH;
            case CuteEarth:
                return message  + " | " + Chat.CUTEEARTH;
            case Phobos:
                return message  + " | " + Chat.PHOBOS;
            case Rusher:
                return message  + " | " + Chat.RUSHER;
            case Future:
                return message  + " | " + Chat.FUTURE;
            case Konas:
                return message  + " | " + Chat.KONAS;
            case GameSense:
                return message  + " | " + Chat.GAMESENSE;
            case KamiBlue:
                return message  + " | " + Chat.KAMIBLUE;
            case Custom:
                return message  + " | " + module.customSuffix.getValue();
        }
        return appendSuffix(message);
    }
}
