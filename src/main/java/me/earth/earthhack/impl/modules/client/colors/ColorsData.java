package me.earth.earthhack.impl.modules.client.colors;

import me.earth.earthhack.api.module.data.DefaultData;

final class ColorsData extends DefaultData<Colors>
{
    public ColorsData(Colors module)
    {
        super(module);
        this.descriptions.put(module.extended,
                "Use or not extended colors");
        this.descriptions.put(module.modulesColorsetting,
                "The module colors.");
        this.descriptions.put(module.moduleHoversetting,
                "The module hovering colors.");
        this.descriptions.put(module.settingColorsetting,
                "The modules internal settings colors.");
        this.register(module.onModulesetting,
                "The color of an active module text.");
        this.register(module.offModulesetting,
                "The color of an inactive module text.");
        this.register(module.topColorsetting,
                "The section color.");
        this.register(module.topBgColorsetting,
                "The section background color.");
        this.register(module.textColorDescsetting,
                "Description text color.");
        this.register(module.catEarsSetting,
                "The CatEars color.");
    }

    @Override
    public int getColor()
    {
        return 0xff34A1FF;
    }

    @Override
    public String getDescription()
    {
        return "Gui colors. This module is always on.";
    }

}
