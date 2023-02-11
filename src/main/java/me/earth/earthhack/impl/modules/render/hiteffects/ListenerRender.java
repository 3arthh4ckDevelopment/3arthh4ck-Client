package me.earth.earthhack.impl.modules.render.hiteffects;

import me.earth.earthhack.impl.event.events.render.Render3DEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;

public class ListenerRender extends ModuleListener<HitEffects, Render3DEvent> {
    public ListenerRender(HitEffects module)
    {
        super(module, Render3DEvent.class);
    }
    public void invoke(Render3DEvent event){

    }
}
