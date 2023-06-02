package me.earth.earthhack.impl.modules.combat.killaura;

import me.earth.earthhack.impl.event.events.render.Render3DEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.combat.killaura.util.AuraRender;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.earthhack.impl.util.render.entity.JelloRender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

public class ListenerRender extends ModuleListener<KillAura, Render3DEvent> {

    public ListenerRender(KillAura module)
    {
        super(module, Render3DEvent.class);
    }
    public void invoke(Render3DEvent event)
    {
        if (module.render.getValue() && module.renderMode.getValue() == AuraRender.Jello)
        {
            EntityPlayer target = module.getPlayerTarget();
            if (target != null
                && mc.player.getDistanceSq(target) <= MathUtil.square(module.range.getValue())
                && InventoryUtil.isHolding(Items.DIAMOND_SWORD) || InventoryUtil.isHolding(Items.DIAMOND_AXE))
            {
                JelloRender.jelloRender(target, module.renderColor.getValue());
            }
        }
    }

}
