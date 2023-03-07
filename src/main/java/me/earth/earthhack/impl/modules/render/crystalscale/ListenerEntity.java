package me.earth.earthhack.impl.modules.render.crystalscale;

import me.earth.earthhack.impl.event.events.render.RenderEntityEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;

final class ListenerEntity extends ModuleListener<CrystalScale, RenderEntityEvent> {
    public ListenerEntity(CrystalScale module)
    {
        super(module, RenderEntityEvent.class);
    }

    @Override
    public void invoke(RenderEntityEvent e)
    {

    }
}
