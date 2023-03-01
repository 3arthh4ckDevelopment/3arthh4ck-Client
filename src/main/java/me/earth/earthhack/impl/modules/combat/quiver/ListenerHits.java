package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ListenerHits extends ModuleListener<Quiver, LivingHurtEvent>
{
    public ListenerHits(Quiver module){
        super(module, LivingHurtEvent.class);
    }

    /*
     * This listener is used only for Quiver HUD-Mode hits.
     *  Not a very good/efficient way to do this, I think, but it works I guess.
     * Something will likely be added here, to make this more useful.
     */
    public void invoke(LivingHurtEvent event)
    {
        if(event.getEntity() instanceof EntityTippedArrow){
            module.hits++;
        }

        // Maybe something else here?
    }
}
