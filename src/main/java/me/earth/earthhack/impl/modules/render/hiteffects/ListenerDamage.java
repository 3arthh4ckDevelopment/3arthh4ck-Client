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
    String[] heroFxText = {"kaboom", "wham", "zap", "boom", "whack", "smash", "knockout"};
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
                EntityLightningBolt bolt = new EntityLightningBolt(mc.player.world, 0D, 0D, 0D, true);
                double LightningPosX = event.getEntity().posX;
                double LightningPosY = event.getEntity().posY;
                double LightningPosZ = event.getEntity().posZ;
                if(module.onlyOnKill.getValue())
                {
                    if(event.getEntity().isDead)
                        bolt.setLocationAndAngles(LightningPosX, LightningPosY, LightningPosZ, 0.0f, 0.0f);
                }
                else
                    bolt.setLocationAndAngles(LightningPosX, LightningPosY, LightningPosZ, 0.0f, 0.0f);
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
}
