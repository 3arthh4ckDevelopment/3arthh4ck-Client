package me.earth.earthhack.impl.modules.misc.chat;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.modules.misc.chat.util.SuffixMode;
import me.earth.earthhack.impl.util.network.NetworkUtil;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ListenerChatMessage extends ModuleListener<Chat, PacketEvent.Send<CPacketChatMessage>> {
    public ListenerChatMessage(Chat module){
        super(module, PacketEvent.Send.class);
    }

    @Override
    public void invoke(PacketEvent.Send<CPacketChatMessage> e){
        if (mc.player == null || mc.world == null || module.suffixMode.getValue() == SuffixMode.None) {
            return;
        }

        String message = e.getPacket().getMessage();

        if (!message.startsWith(Commands.getPrefix())) {
            if (module.suffixWhispers.getValue() && message.matches("^/(msg|r|w|tell|l)")) {
                e.setCancelled(true);
            } else if (!message.startsWith("/")) {
                e.setCancelled(true);
            }

            String suffix = module.suffixMode.getValue() == SuffixMode.Custom ? module.customSuffix.getValue() : module.suffixMode.getValue().getSuffix();
            NetworkUtil.sendPacketNoEvent(new CPacketChatMessage(message  + " | " + suffix));
        }
    }
}
