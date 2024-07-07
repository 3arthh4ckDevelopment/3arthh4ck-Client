package me.earth.earthhack.impl.modules.render.norender;

import me.earth.earthhack.impl.event.events.render.RenderEntityEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.entity.player.EntityPlayer;

final class ListenerRenderEntities extends
        ModuleListener<NoRender, RenderEntityEvent.Pre>
{
    public ListenerRenderEntities(NoRender module)
    {
        super(module, RenderEntityEvent.Pre.class);
    }

    @Override
    public void invoke(RenderEntityEvent.Pre event)
    {
        if (module.entities.getValue()
                || (module.spectators.getValue()
                    && event.getEntity() instanceof EntityPlayer
                    && ((EntityPlayer) event.getEntity()).isSpectator()))
            // no need for self check I guess, someone might like it though
        {
            event.setCancelled(true);
        }
    }

}
