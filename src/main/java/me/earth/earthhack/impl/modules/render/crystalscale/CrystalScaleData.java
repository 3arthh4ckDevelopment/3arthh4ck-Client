package me.earth.earthhack.impl.modules.render.crystalscale;

import me.earth.earthhack.api.module.data.DefaultData;

public class CrystalScaleData extends DefaultData<CrystalScale> {
    public CrystalScaleData(CrystalScale module){
        super(module);
        register(module.scale, "Set the scale of End Crystals.");
        register(module.animate, "Animates the spawning of End Crystals." +
                " This looks like the Crystal is growing from the ground.");
        register(module.time, "The time the spawn animation should take.");
    }

    public String getDescription(){
        return "Modifies End Crystal's visual properties, like scale & spawn animation.";
    }
}
