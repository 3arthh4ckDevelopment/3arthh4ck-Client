package me.earth.earthhack.impl.modules.client.compatibility;

import me.earth.earthhack.impl.event.events.network.MotionUpdateEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.minecraft.MovementUtil;

final class ListenerMotion extends ModuleListener<Compatibility, MotionUpdateEvent>
{
    public ListenerMotion(Compatibility module)
    {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void invoke(MotionUpdateEvent event)
    {
        if (module.swiftSneak.getValue()) {
            if (mc.player.isSneaking())
                MovementUtil.setMoveSpeed(0.12);
        }
    }

}
