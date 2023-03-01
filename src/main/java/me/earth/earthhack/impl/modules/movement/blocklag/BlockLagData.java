package me.earth.earthhack.impl.modules.movement.blocklag;

import me.earth.earthhack.api.module.data.DefaultData;

final class BlockLagData extends DefaultData<BlockLag>
{
    public BlockLagData(BlockLag module)
    {
        super(module);
        register(module.vClip,
                "V-clips the specified amount down to cause a lagback." +
                        " Don't touch, 9 should be perfect.\n" +
                        "For modes Constant and Smart.");
        register(module.timerAmount, "What the timer should be set to.");
        register(module.motionAmount, "Amount of motion the player should get.");
        register(module.useTimer, "Use timer when attempting lagback to" +
                        " make it easier to accomplish.\n" +
                        "NOTE: This page's settings will only" +
                        " apply, if you are using \"Bypass\" as Offsets mode!");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "The OG Burrow.";
    }

}