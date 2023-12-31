package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.api.module.data.DefaultData;

public class BlockerData extends DefaultData<Blocker> {

    public BlockerData(Blocker module) {
        super(module);
        register(module.pages, "Structures the Settings in the ClickGUI");
        register(module.holeCheck, "Checks if you are in a Hole before placing blocks.");
        register(module.onlyTarget, "Checks if you have enemies near you before placing blocks.");
        register(module.placeDelay, "Delay between block placements.");
        register(module.sides, "Places blocks on your sides.");
        register(module.face, "Faces blocks around your head.");
        register(module.modeSides, "Detection mode for Side blocks." +
                "\n- Touch : Places blocks when they are touched." +
                "\n- Break : Place blocks when they have been broken.");

        register(module.cev, "Prevents crystals being placed on top of you.");
        register(module.modeCev, "Detection mode for Blocks above your Head." +
                "\n- Touch : Places blocks on top of your Head when the block has been " +
                "touched. (Recommended)" +
                "\n- Break : Places blocks on top of your Head after the block has been broken." +
                " Will likely allow the enemy to bomb you once, so Touch is better for most cases.");
        register(module.stepCev, "Checks if the position above the Block over your head is" +
                " blocked.");
        register(module.detectTime, "Time for DetectionMode - Touch to count as a detection.");

    }
}
