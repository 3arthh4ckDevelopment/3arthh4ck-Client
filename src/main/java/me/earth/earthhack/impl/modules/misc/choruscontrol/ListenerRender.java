package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.render.WorldRenderEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.render.RenderUtil;

public class ListenerRender extends ModuleListener<ChorusControl, WorldRenderEvent> {
    public ListenerRender(ChorusControl module){
        super(module, WorldRenderEvent.class);
    }

    public void invoke(WorldRenderEvent e) // Still a bit inspired from GS++?
    {
        if(module.cancelled)
            RenderUtil.drawBox(mc.player.getEntityBoundingBox(), module.espColor.getValue());
    }
}
