package me.earth.earthhack.impl.modules.combat.blocker;

import java.util.ArrayList;
import me.earth.earthhack.impl.event.events.misc.UpdateEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.math.StopWatch;
import net.minecraft.util.math.BlockPos;

public class ListenerUpdate extends ModuleListener<Blocker, UpdateEvent> {
    protected static ArrayList<BlockPos> scheduledPlacements = new ArrayList<>();
    StopWatch timer = new StopWatch();

    public ListenerUpdate(Blocker module) {
        super(module, UpdateEvent.class, 1);
    }

    @Override
    public void invoke(UpdateEvent event) {
        if (timer.passed(module.surroundDelay.getValue())) {
            timer.reset();
            scheduledPlacements.clear();
        }
    }

}
