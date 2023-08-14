package me.earth.earthhack.api.hud.data;

import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.util.AbstractData;

public class DefaultHudData<M extends HudElement> extends AbstractData<M>
{
    public DefaultHudData(M module)
    {
        super(module);
    }

    @Override
    public String getDescription()
    {
        return "HUD module.";
    }

    @Override
    public int getColor()
    {
        return 0xeeeeeeee;
    }

    /*
    @Override
    public String[] getAliases() {
        return new String[]{};
    }
    it will be used with search
     */

}
