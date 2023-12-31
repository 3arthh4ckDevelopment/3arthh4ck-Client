package me.earth.earthhack.impl.modules.render.crystalchams;

import me.earth.earthhack.api.module.data.DefaultData;

public class CrystalChamsData extends DefaultData<CrystalChams> {
    public CrystalChamsData(CrystalChams module){
        super(module);
        register(module.mode, "Set the chams mode");
        register(module.chams, "Turn on or off chams");


        register(module.scale, "Set the scale of End Crystals.");
        register(module.animate, "Animates the spawning of End Crystals." +
                " This looks like the Crystal is growing from the ground.");
        register(module.time, "The time the spawn animation should take.");
    }

    public String getDescription(){
        return "Modifies End Crystal's visual properties, like scale & spawn animation.";
    }
}
