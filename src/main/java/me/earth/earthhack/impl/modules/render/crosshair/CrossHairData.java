package me.earth.earthhack.impl.modules.render.crosshair;

import me.earth.earthhack.api.module.data.DefaultData;

final class CrossHairData extends DefaultData<CrossHair>
{
    public CrossHairData(CrossHair module)
    {
        super(module);
        register(module.crossHair, "Turn on or off the crosshair");
        register(module.indicator, "Turn on or off the attack indicator");
        register(module.outline, "Turn on or off the crosshair outline");
        register(module.dot, "Adds a dot");
        register(module.dotColor, "The dot color");
        register(module.dotRadius, "The dot radius");
        register(module.gapMode, "The gap mode");
        register(module.color, "The internal crosshair color");
        register(module.outlineColor, "The outline color");
        register(module.length, "Line length");
        register(module.width, "Line width");
        register(module.gapSize, "The gap radius");

        register(module.pvpInfo, "Turn on or off the pvp infos");
        register(module.hudSync, "Sync the color with the hud");
        register(module.scale, "The text scale");
        register(module.offset, "The text offset");
        register(module.textDistance, "The text distance");
        register(module.info, "Show modules infos");
        register(module.PvpInfoColor, "The text color");
    }

    @Override
    public int getColor()
    {
        return 0xffba4180;
    }

    @Override
    public String getDescription()
    {
        return "Changes the appearance of the crosshair";
    }

}
