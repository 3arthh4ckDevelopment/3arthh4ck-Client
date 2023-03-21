package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.api.module.data.DefaultData;

public class ChorusControlData extends DefaultData<ChorusControl> {
    public ChorusControlData(ChorusControl module) {
        super(module);
        register(module.esp, "Draws an ESP to where you're going to teleport.");
        register(module.espMode, "How the ESP should be drawn");
        register(module.espColor, "What color the ESP should be drawn with");
    }

    public String getDescription(){
        return "Shouldn't be used, very bad and needs rewrite.";
    }
}
