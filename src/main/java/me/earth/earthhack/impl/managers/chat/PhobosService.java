package me.earth.earthhack.impl.managers.chat;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.util.text.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;

import java.util.ArrayList;
import java.util.List;

// phobos.crypt messages (zane said it was never implemented, so i won't make it ig...)
//TODO: test

public class PhobosService extends SubscriberImpl implements Globals {

    List<EntityPlayer> requests = new ArrayList<>();

    public PhobosService() {
        this.listeners.add(new LambdaListener<>(PacketEvent.Receive.class, e -> {
            if (e.getPacket() instanceof SPacketChat) {
                SPacketChat packet = (SPacketChat) e.getPacket();
                String message = packet.chatComponent.getUnformattedText().toLowerCase();
                if (message.contains("phobos.confirm##1337") && (message.contains("whispers") || message.contains("says"))) {
                    if (message.substring(message.indexOf(':')).contains("phobos.confirm##1337") // 2 checks for the same thing...
                            && (message.substring(0, message.indexOf(':')).contains("whispers") || message.substring(0, message.indexOf(':')).contains("says"))) {

                        List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
                        players.sort((s1, s2) -> Integer.compare(s2.getName().length(), s1.getName().length()));

                        for (EntityPlayer p : players) {
                            if (p == mc.player) continue;
                            if (packet.chatComponent.getUnformattedText().contains(p.getName()) && !p.getName().equals(mc.player.getName())) {
                                if (!requests.contains(p)) {
                                    Earthhack.getLogger().info(p.getName() + " confirmed your phobos!");
                                    ChatUtil.sendMessage("/msg " + p.getName() + " I too am using the best client for crystal pvp! (" + Earthhack.NAME + " - " + Earthhack.VERSION + ")", 514086);
                                    ChatUtil.deleteMessage(514086);

                                    requests.add(p);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }));
    }

}
