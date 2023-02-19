package me.earth.earthhack.impl.modules.movement.longjump;

import me.earth.earthhack.api.module.data.DefaultData;

final class LongJumpData extends DefaultData<LongJump>
{
    public LongJumpData(LongJump module)
    {
        super(module);
        register(module.mode, "-Normal best for Anarchy\n-Cowabunga ...");
        register(module.boost, "Amount your jump will be boosted by.");
        register(module.noKick, "Prevents you from getting kicked by" +
                " disabling this module automatically.");
        register(module.pauseSpeed, "Pauses Speed while jumping, so you" +
                " don't get lagged back.");
        register(module.speedCheck, "Takes into account that you may not" +
                " have speed enabled when enabling" +
                " LongJump. When enabled, speed will not enable" +
                " after LongJump is finished, eventhough PauseSpeed" +
                "is on.");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Makes you jump far.";
    }

}
