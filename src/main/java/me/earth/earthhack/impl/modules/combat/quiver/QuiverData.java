package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.data.DefaultData;
import me.earth.earthhack.impl.modules.combat.quiver.Quiver;

public class QuiverData extends DefaultData<Quiver> {
    public QuiverData(Quiver module){
        super(module);
        register(module.effectArrows, "Only allows arrows with positive"
                + " effects to be used by Quiver.");
        register(module.delay,"Delay between shooting an arrow in ticks.");
        register(module.spam,"Ignore delay completely and shoot arrows" +
                " as fast as possible.");
        register(module.tpsSync,"Attempts to sync arrows with the server tps,"
                + " may result in better performance. Can also"
                + " cause issues.");
        register(module.rotateMode, "Select mode to use when rotating."
        + "\nNormal - Normal rotations"
        + "\nPacket - Rotate with packets, may seem faster");
        register(module.hudMode, "What kind of information the HUD should show"
        + "\nArrows - Show how many arrows you can shoot"
        + "\nHits - Count how many arrows actually hit you");
    }

    @Override
    public String getDescription()
    {
        return "Shoots yourself with arrows. In development!!!!";
    }
}
