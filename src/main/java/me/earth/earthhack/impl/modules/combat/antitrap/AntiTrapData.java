package me.earth.earthhack.impl.modules.combat.antitrap;

import me.earth.earthhack.impl.util.helpers.blocks.data.ObbyData;

final class AntiTrapData extends ObbyData<AntiTrap>
{
    public AntiTrapData(AntiTrap module)
    {
        super(module);
        register(module.mode, "- Crystal : Places crystals around you so you can't" +
                " get trapped.\n" +
                "- FacePlace : Like Surround, but a block higher to prevent faceplacing.\n" +
                "- Fill : Fills spots where crystals around you could be placed.\n " +
                "- Bomb : Like SelfTrap, but extends a block higher to prevent CevBreak.");
        register(module.offhand, "Switches to the Offhand.");
        register(module.smart, "Very intelligent. Waits for blocks being mined before placing.");
        register(module.timeOut, "Interval between toggling this module." +
                " (for fat fingers)");
        register(module.empty, "For Mode-Crystal: Disable the module if no " +
                "suitable position can be found. Otherwise it will run until " +
                "it can place a crystal.");
        register(module.swing, "If you want to see your hand swinging or not.");
        register(module.highFill, "For Mode-Faceplace/Fill: If all feettrap" +
                " positions should be filled 2 blocks high.");
        register(module.confirm,
                "Time for the server to confirm block placements.");
        register(module.autoOff, "Whether or not the module should automatically disable" +
                " after placing blocks.");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Places a crystal next to you to block possible AutoTraps.";
    }
}
