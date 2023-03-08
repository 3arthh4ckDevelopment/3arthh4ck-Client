package me.earth.earthhack.impl.modules.render.crystalscale;

import me.earth.earthhack.api.module.data.DefaultData;

public class CrystalScaleData extends DefaultData<CrystalScale> {
    public CrystalScaleData(CrystalScale module){
        super(module);
        register(module.scale, "Set the scale of End Crystals.");
        register(module.animate, "Animates the spawning of End Crystals." +
                " This looks like the Crystal is growing from the ground.");
        register(module.time, "The time the animation should take.");
        register(module.modify, "Whether or not the crystal's normal animation" +
                " should be modified.");
        register(module.spinSpeed, "The modifier for the Crystal's spinning speed.");
        register(module.bounceSpeed, "The modifier for the Crystal's bouncing speed.");
    }

    public String getDescription(){
        return "Modifies End Crystal's visual properties, like scale, spawn animation and" +
                " spin/bounce speed.";
    }
}
