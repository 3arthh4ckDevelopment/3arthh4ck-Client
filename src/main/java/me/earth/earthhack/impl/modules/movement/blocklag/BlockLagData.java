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
        register(module.timerAmount, "What the timer should be set to. In development!!!!");
        register(module.motionAmount, "Amount of motion the player should get.");
        register(module.useTimer, "Use timer when attempting lagback to" +
                        " make it easier to accomplish. In development!!!!");
        register(module.useBlink, "New revolutionary bypass for Crystalpvp.cc." +
                " Temporarily disables sending any CPacketPlayer packets, so" +
                " you can burrow in this time. In development, but functional!");
        register(module.blinkDuration, "How long Blink should be used for. For cc," +
                " this is pretty good and stable at around 1000ms." +
                " Not having this high enough can result in failure." +
                " Having it too high can result in disconnecting, or" +
                " failure.\nNot yet implemented!!!!");
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