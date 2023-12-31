package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.api.module.data.DefaultData;

public class ChorusControlData extends DefaultData<ChorusControl> {
    public ChorusControlData(ChorusControl module) {
        super(module);
        register(module.esp, "Draws an ESP to where you're going to teleport.");
        register(module.espMode, "ESP mode.\n" +
                "- Box : A standard box ESP on the TP location.\n" +
                "- Chams : Draws your PlayerModel on the TP location.");
        register(module.hudMode, "Whether or not information should be displayed in the HUD.\n" +
                "- Info : Shows whether or not you've ignored a teleport in the HUD.\n" +
                "- None : Shows no information. Default in most clients.");
        register(module.color, "The fill color of the ESP.");
        register(module.outline, "The outline color of the ESP.");
        register(module.pulseCycle, "The time for 1 pulse cycle to run. Leave at 0 to disable.");
        register(module.yAnimations, "For mode Chams:\n" +
                "Animates the PlayerModel to move upwards. Not really practical I guess," +
                " but looks pretty cool. Wouldn't recommend enabling if you have slow" +
                " reflexes.");
        register(module.autoOffDelay, "The delay after which ChorusControl will" +
                " be disabled. Leave at 0 to disable.");
    }

    public String getDescription(){
        return "Allows for ignoring or postponing the Chorus Fruit teleport. " +
                "In development!!!!";
    }
}
