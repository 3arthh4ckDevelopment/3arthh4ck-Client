package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.misc.EatEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ListenerChorus extends ModuleListener<ChorusControl, EatEvent> {
    public ListenerChorus(ChorusControl module){
        super(module, EatEvent.class);
    }

    public void invoke(EatEvent e)
    {
        if(e.getStack().equals(new ItemStack(Items.CHORUS_FRUIT)))
            module.valid = true;

        if(module.onlySneak.getValue())
                module.valid = mc.gameSettings.keyBindSneak.isPressed();
    }
}
