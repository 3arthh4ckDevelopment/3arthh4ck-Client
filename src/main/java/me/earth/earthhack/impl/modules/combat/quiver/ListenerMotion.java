package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.impl.event.events.network.MotionUpdateEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.minecraft.blocks.mine.MineUtil;
import net.minecraft.init.Blocks;

public class ListenerMotion extends ModuleListener<Quiver, MotionUpdateEvent> {
    // Used for checking for CheckBlocked and MineBlocked.
    public ListenerMotion(Quiver module)
    {
        super(module, MotionUpdateEvent.class);
    }
    public void invoke(MotionUpdateEvent e)
    {
        if(mc.player != null && mc.world != null)
        {
            if(module.blockedCheck.getValue())
            {
                module.isBlocked = mc.world.getBlockState(module.blockedPosition).getBlock() != Blocks.AIR
                        && MineUtil.canBreak(module.blockedPosition);
            }
        }
    }
}
