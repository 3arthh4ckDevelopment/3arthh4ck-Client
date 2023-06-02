package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.impl.util.helpers.blocks.util.TargetResult;
import me.earth.earthhack.impl.util.helpers.blocks.ObbyListener;

public class ListenerObsidian extends ObbyListener<Blocker>
{
    public ListenerObsidian(final Blocker module) {
        super(module, 10);
    }

    @Override
    protected TargetResult getTargets(final TargetResult result) {
        result.setTargets(ListenerUpdate.scheduledPlacements);
        return result;
    }

    @Override
    public void onModuleToggle() {
        super.onModuleToggle();
    }
}
