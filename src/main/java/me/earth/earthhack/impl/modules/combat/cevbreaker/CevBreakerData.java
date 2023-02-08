package me.earth.earthhack.impl.modules.combat.cevbreaker;

import me.earth.earthhack.api.module.data.DefaultData;

public class CevBreakerData extends DefaultData<CevBreaker> {
    public CevBreakerData(CevBreaker cb){
        super(cb);
        register(cb.mode, "CevBreaker mode." +
                "\nNormal: Completes a complete cycle of placing crystal, mining," +
                ", breaking and placing obby and repeat." +
                "\nInstant: Tries to instantly break obsidian and placing a crystal and repeating." +
                "Likely doesn't work on newer servers.");
        register(cb.range, "Select a maximum range at which CevBreaker" +
                " can place/break etc.");
        register(cb.toggleAt, "Not implemented yet.");
        register(cb.enemyRange, "Players within this range will be " +
                "considered enemies, and attacked.");
        register(cb.delay, "Delay between redoing a cycle after finishing the first one.");
        register(cb.cooldown,"For servers that require you to wait a little " +
                "before starting to attack after switching hand");
        register(cb.rotate, "Whether or not to rotate");
        register(cb.reCheckCrystal, "Performs extra checks to see if a crystal already exists");
        register(cb.airCheck, "Checks whether or not the block above the target is " +
                "already air, and whether or not to explode a crystal");
        register(cb.smartSneak, "Sends sneak packets depending on situation");
        register(cb.bypass, "Performs a swap action when Speedmine has finished mining" +
                " so you don't have to hold your pickaxe");
    }
}
