package me.earth.earthhack.impl.modules.client.notifications;

import me.earth.earthhack.api.event.events.Stage;
import me.earth.earthhack.impl.event.events.network.EntityChunkEvent;

import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.entity.player.EntityPlayer;

final class ListenerPlayerLeave extends ModuleListener<Notifications, EntityChunkEvent> {
    public ListenerPlayerLeave(Notifications module) {
        super(module, EntityChunkEvent.class);
    }

    @Override
    public void invoke(EntityChunkEvent event) {
        if (event.getStage() == Stage.POST && event.getEntity() != null) {
            if (event.getEntity() instanceof EntityPlayer) {
                {
                    module.onPlayerLeave(event.getEntity());
                }
            }
        }
    }
}
