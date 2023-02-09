package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.data.DefaultData;
import me.earth.earthhack.impl.modules.combat.quiver.Quiver;

public class QuiverData extends DefaultData<Quiver> {
    public QuiverData(Quiver module){
        super(module);
        register(module.effectArrows, "Only allows arrows with effects"
                + "to be used by Quiver.");
        register(module.delay,"Delay between shooting an arrow in ticks.");
        register(module.spam,"Ignore delay completely and shoot as many" +
                "arrows as possible.");
        register(module.tpsSync,"Attempts to sync arrows with the server tps," +
                " may result in better performance.");
    }
}
