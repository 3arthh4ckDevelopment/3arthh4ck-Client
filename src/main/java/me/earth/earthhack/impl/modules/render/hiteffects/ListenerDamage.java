package me.earth.earthhack.impl.modules.render.hiteffects;

import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.math.StopWatch;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

public class ListenerDamage extends ModuleListener<HitEffects, LivingHurtEvent> {
    public ListenerDamage(HitEffects module)
    {
        super(module, LivingHurtEvent.class);
    }

    // SUPERHERO FX
    Random rnd = new Random();
    String fxHero;
    Color fxColor;
    boolean firstDraw;
    String[] heroFxText = {"kaboom", "wham", "zap", "boom", "whack", "smash", "knockout", "wow", "pow", "power", "rekt"};
    Color[] heroFxColor = {Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.ORANGE};

    // TIMERS
    StopWatch renderTimer = new StopWatch();
    @SideOnly(Side.CLIENT) // makes this client-sided :^)
    public void invoke(LivingHurtEvent event)
    {
        if(module.onlyTargets.getValue())
        {
            if(module.lightning.getValue())
            {
                if(module.onlyOnKill.getValue())
                {
                    if(event.getEntity().isDead)
                        mc.world.spawnEntity(new EntityLightningBolt(mc.player.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, true));
                }
                else
                    mc.world.spawnEntity(new EntityLightningBolt(mc.player.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, true));
            }
            if(module.superheroFx.getValue())
            {
                if(firstDraw)
                {
                    renderTimer.reset();
                    firstDraw = false;
                }

            }
        }
    }

    public String SuperheroParticle(){
        fxHero = heroFxText[rnd.nextInt(heroFxText.length)]; // Get a randomized string, like 'kaboom', from our array
        fxColor = heroFxColor[rnd.nextInt(heroFxColor.length)];

        return fxHero;
    }
    
    public Color SuperheroColor(){ // return a random color from our array
        fxColor = heroFxColor[rnd.nextInt(heroFxColor.length)];

        return fxColor;
    }

    public void drawHeroFx()
    {
        //TODO: This, with some fun settings
    }
}
