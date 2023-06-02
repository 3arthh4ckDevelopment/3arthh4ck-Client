package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.misc.EatEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ListenerEat extends ModuleListener<ChorusControl, EatEvent> {
    public ListenerEat(ChorusControl module){
        super(module, EatEvent.class);
    }

    public void invoke(EatEvent e)
    {
        if(e.getStack().equals(new ItemStack(Items.CHORUS_FRUIT))){
            module.valid = true;

            module.x = (int) mc.player.posX;
            module.y = (int) mc.player.posY;
            module.z = (int) mc.player.posZ;
        }
    }
}
