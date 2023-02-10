package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.data.DefaultData;
public class QuiverData extends DefaultData<Quiver> {
    public QuiverData(Quiver module){
        super(module);
        register(module.delay,"Delay between shooting an arrow in ticks.");
        register(module.spam,"Ignore delay completely and shoot arrows" +
                " as fast as possible.");
        register(module.tpsSync,"Attempts to sync arrows with the server tps,"
                + " may result in better performance. Can also"
                + " cause issues.");
        register(module.rotateMode, "Select mode to use when rotating."
                + "\nNormal - Normal rotations"
                + "\nPacket - Rotate with packets, may seem faster, but can cause"
                + " lagging back.");
        register(module.hudMode, "What kind of information the HUD should show"
                + "\nArrows - Show how many arrows you can shoot"
                + "\nHits - Count how many arrows actually hit you");
        register(module.quiverMode, "Select how quiver should do things"
                + "\nAutomatic - Automatically shoot yourself with arrows"
                + "\nManual - Manually shoot yourself with arrows");
        register(module.blockedCheck, "Checks if you have anything above"
                + " your head preventing a successful hit");
        register(module.mineBlocked, "If BlockedCheck finds a block, this"
                + " will automatically mine the block, to force a"
                + "successful hit.");
    }
    @Override
    public String getDescription()
    {
        return "Shoots yourself with arrows that have positive effects. In development!!!!";
    }
}
