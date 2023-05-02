package me.earth.earthhack.impl.modules.player.sorter;

import me.earth.earthhack.api.module.data.DefaultData;

public class SorterData extends DefaultData<Sorter> {
    public SorterData(Sorter module)
    {
        super(module);
        register(module.switchBack, "Sometimes sorter switches to another slot in your inventory."
                + " This setting prevents that.");
        register(module.virtualHotbar, "If you are using CooldownBypass mode Pick, " +
                " this is necessary for it to function properly.");
    }



}
