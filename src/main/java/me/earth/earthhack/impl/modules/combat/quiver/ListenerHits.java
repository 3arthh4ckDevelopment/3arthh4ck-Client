package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
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
        if(event.getEntity() instanceof EntityPlayer){
            if(event.getSource() == DamageSource.MAGIC) // Don't know if this actually works, but tipped arrows should technically count as magic
                module.hits++;
        }

        // Maybe something else here?
    }
}
