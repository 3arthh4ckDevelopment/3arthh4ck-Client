package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.data.DefaultData;
public class QuiverData extends DefaultData<Quiver> {
    public QuiverData(Quiver module){
        super(module);
        register(module.delay,"Minecraft Bow mechanics work like this:\n" +
                "When you start drawing back your bow, it takes" +
                " 0.1 seconds or 100 milliseconds to shoot one" +
                " arrow at minimal (typically 1hp) damage." +
                " Quiver, by default, draws back for 100ms, and then adds" +
                " this settings value to the delay. This can be a sort-of" +
                " failsafe and keep Quiver from failing.");
        register(module.fast,"Ignore delay completely and shoot arrows" +
                " as fast as possible.");
        register(module.rotateMode, "Select mode to use when rotating."
                + "\nNormal - Normal rotations."
                + "\nPacket - Rotate with packets, may seem faster, but can cause"
                + " lagging back.");
        register(module.hudMode, "What kind of information the HUD should show"
                + "\nArrows - Show how many arrows you can shoot."
                + "\nHits - Count how many arrows actually hit you.");
        register(module.quiverMode, "Select how quiver should do things"
                + "\nAutomatic - Automatically shoot yourself with arrows."
                + "\nManual - Manually shoot yourself with arrows. When using mode" +
                " \"Manual\", setting Rotation to Client is recommended.");
        register(module.blockedCheck, "Checks if you have anything above"
                + " your head preventing a successful hit. In development!!!!");
        register(module.mineBlocked, "If BlockedCheck finds a block, this"
                + " will automatically mine the block, to force a"
                + " successful hit. In development!!!!");
    }
    @Override
    public String getDescription()
    {
        return "Shoots yourself with arrows that have positive effects. In development!!!!";
    }
}
