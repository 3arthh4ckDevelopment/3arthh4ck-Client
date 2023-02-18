package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.impl.event.events.misc.EatEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.render.RenderUtil;
import net.minecraft.init.Items;


final class ListenerEatChorus extends ModuleListener<ChorusControl, EatEvent> {
    public ListenerEatChorus(ChorusControl module)
    {
        super(module, EatEvent.class);
    }

    @Override
    public void invoke(EatEvent event) {
        if(mc.player.getHeldItemMainhand().equals(Items.CHORUS_FRUIT) || mc.player.getHeldItemOffhand().equals(Items.CHORUS_FRUIT)){
            module.eatingChorus = true;
            module.startCancel = true;
            module.x = (int) mc.player.posX;
            module.y = (int) mc.player.posY;
            module.z = (int) mc.player.posZ;
            RenderUtil.drawBox(mc.player.getEntityBoundingBox(), module.espColor.getValue());
        } else {
            module.eatingChorus = false;
            module.startCancel = false;
        }
    }
}
