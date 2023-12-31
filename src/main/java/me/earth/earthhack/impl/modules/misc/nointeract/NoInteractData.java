package me.earth.earthhack.impl.modules.misc.nointeract;

import me.earth.earthhack.api.module.data.DefaultData;

public class NoInteractData extends DefaultData<NoInteract> {
    public NoInteractData(NoInteract module){
        super(module);
        register(module.sneak, "Allows interactions if sneaking.");
        register(module.tileOnly, "Only disables interactions with Tile Entities.");
    }

    @Override
    public String getDescription(){
        return "Removes interactions with blocks.";
    }
}
