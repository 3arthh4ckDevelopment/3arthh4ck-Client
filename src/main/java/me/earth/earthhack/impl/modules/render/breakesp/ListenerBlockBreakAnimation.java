package me.earth.earthhack.impl.modules.render.breakesp;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.math.DistanceUtil;
import me.earth.earthhack.impl.util.text.ChatIDs;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;

public class ListenerBlockBreakAnimation extends ModuleListener<BreakESP, PacketEvent.Receive<SPacketBlockBreakAnim>>
{
    public ListenerBlockBreakAnimation(final BreakESP module) {
        super(module, PacketEvent.Receive.class, SPacketBlockBreakAnim.class);
    }

    public void invoke(final PacketEvent.Receive<SPacketBlockBreakAnim> event) {

        if (event.getPacket().getProgress() != 255) {
            if (Math.sqrt(DistanceUtil.distanceSq(event.getPacket().getPosition().getX(), event.getPacket().getPosition().getY(), event.getPacket().getPosition().getZ(), mc.player.posX, mc.player.posY, mc.player.posZ)) < module.radius.getValue()) {
                BreakESPBlock exists = null;
                if (module.blocks.size() != 0) {
                    for (BreakESPBlock b : module.blocks)
                        if (b.entityID == event.getPacket().getBreakerId())
                            exists = b;
                }

                if (exists != null)
                {
                    if (exists.blockPos != event.getPacket().getPosition()) {
                        module.blocks.remove(exists);
                        exists = null;
                    }
                    else if (event.getPacket().getProgress() == 10) {
                        module.blocks.remove(exists);
                    }
                }
                if (exists == null
                        && mc.world.getBlockState(event.getPacket().getPosition()).getBlock() != Blocks.BEDROCK
                        && mc.world.getBlockState(event.getPacket().getPosition()).getBlock() != Blocks.AIR
                        && mc.world.getBlockState(event.getPacket().getPosition()).isFullBlock())
                {
                    module.blocks.add(new BreakESPBlock(event.getPacket().getPosition(), event.getPacket().getBreakerId(), System.currentTimeMillis()));
                    if (module.chatPos.getValue())
                        Managers.CHAT.sendDeleteMessage(TextColor.AQUA + event.getPacket().getPosition(), String.valueOf(module.random.nextInt()), ChatIDs.MODULE);
                }
            }
        }
    }

}