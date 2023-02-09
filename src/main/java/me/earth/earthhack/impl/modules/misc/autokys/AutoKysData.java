package me.earth.earthhack.impl.modules.misc.autokys;

import me.earth.earthhack.api.module.data.DefaultData;

public class AutoKysData extends DefaultData<AutoKys>{
    public AutoKysData(AutoKys module){
        super(module);
        register(module.delay, "Delay in milliseconds before sending the command.");
        register(module.kysCommand, "This depends on server, generally it should be"
                                    + "/kill. This is the command that kills you.");
    }
}
