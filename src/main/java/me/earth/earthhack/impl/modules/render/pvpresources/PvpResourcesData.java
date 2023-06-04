package me.earth.earthhack.impl.modules.render.pvpresources;

import me.earth.earthhack.api.module.data.DefaultData;

final class PvpResourcesData extends DefaultData<PvpResources> {
    public PvpResourcesData(PvpResources module) {
        super(module);
        register(module.style, "The render style.");
        register(module.pretty, "Use rounded corners or no.");
        register(module.x, "The x position.");
        register(module.y, "The y position.");
    }

    @Override
    public int getColor() {
        return 0xffffffff;
    }

    @Override
    public String getDescription() {
        return "Shows how many pvp items you have.";
    }
}