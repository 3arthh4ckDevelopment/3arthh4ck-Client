package me.earth.earthhack.impl.modules.combat.cevbreaker;

import me.earth.earthhack.impl.event.events.network.MotionUpdateEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;

final class ListenerMotion extends ModuleListener<CevBreaker, MotionUpdateEvent> {

    public ListenerMotion(CevBreaker module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void invoke(MotionUpdateEvent event) {
        module.doCevBreaker(event);
    }

}
