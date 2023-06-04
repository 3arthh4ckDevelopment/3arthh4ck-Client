package me.earth.earthhack.impl.modules.render.targethud;

import me.earth.earthhack.api.module.data.DefaultData;

final class TargetHudData extends DefaultData<TargetHud>
{
    public TargetHudData(TargetHud module)
    {
        super(module);
        register(module.bgColor,
                "The background color.");
        register(module.fColor,
                "Text color.");
        register(module.style,
                "The TargetHud style");
        register(module.targeting,
                "Only shows TargetHud if AutoCrystal or Killaura target is found.");
        register(module.maxSetting,
                "Player detect range.");
        register(module.posX,
                "The x position of the TargetHud.");
        register(module.posY,
                "The x position of the TargetHud.");
        register(module.ping,
                "Shows the player ping.");
        register(module.distance,
                "Shows the player distance.");
        register(module.phase,
                "Shows if the player is phased.");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Searches for certain blocks in render distance.";
    }
}
