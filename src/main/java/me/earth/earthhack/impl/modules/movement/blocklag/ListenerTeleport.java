package me.earth.earthhack.impl.modules.movement.blocklag;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.minecraft.blocks.BlockUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
public class ListenerTeleport extends ModuleListener<BlockLag, PacketEvent.Post<CPacketConfirmTeleport>> {
    public ListenerTeleport(BlockLag module) {
        super(module, PacketEvent.Post.class, CPacketConfirmTeleport.class);
    }

    @Override
    public void invoke(PacketEvent.Post<CPacketConfirmTeleport> event) {
        EntityPlayerSP player = mc.player;
        if (player != null
                && module.onTeleport.getValue()
                && !module.blockTeleporting
                && module.ateChorus) {
            if(!ListenerTick.burrow.isEnabled()){
                ListenerTick.burrow.enable();
                module.ateChorus=false;
                if(module.chorusDisable.getValue()
                        || !BlockUtil.isReplaceable(
                        module.pos.add(0,0.2,0)))
                    ListenerTick.burrow.disable();
            }
        }
    }
}
