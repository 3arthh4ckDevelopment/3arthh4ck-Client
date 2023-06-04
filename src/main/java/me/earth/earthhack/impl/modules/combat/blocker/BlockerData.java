package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.impl.util.helpers.blocks.data.BlockPlacingData;

public class BlockerData extends BlockPlacingData<Blocker> {
    public BlockerData(Blocker module) {
        super(module);
        this.register(module.antiCev, "Whether or not Blocker should account for CevBreak.");
        this.register(module.range, "Range from position mined to closest enemy");
        this.register(module.fullExtend, "Full extend (diag, face and normal)");
        this.register(module.extendxyz, "Diag and normal extend when fullextend is off");
        this.register(module.mode, "The detection mode for surrounding blocks");
        this.register(module.modeCev, "The detection mode for the top block");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Places extra blocks around your Surround to prevent it being broken.";
    }
}
