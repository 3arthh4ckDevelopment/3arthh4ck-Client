package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.misc.EatEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.init.Items;


final class ListenerEatChorus extends ModuleListener<ChorusControl, EatEvent> {
    public ListenerEatChorus(ChorusControl module)
    {
        super(module, EatEvent.class);
    }

    @Override
    public void invoke(EatEvent event) {
        module.eatingChorus = mc.player.getHeldItemMainhand().equals(Items.CHORUS_FRUIT) || mc.player.getHeldItemOffhand().equals(Items.CHORUS_FRUIT);
    }
}
