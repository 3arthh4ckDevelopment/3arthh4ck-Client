package me.earth.earthhack.impl.modules.render.popchams;

import me.earth.earthhack.api.module.data.DefaultData;

public class PopChamsData extends DefaultData<PopChams>
{
    public PopChamsData(PopChams module)
    {
        super(module);
        register(module.color, "Chams color");
        register(module.outline, "Chams outline color");
        register(module.lineWidth, "The line width");
        register(module.fadeTime, "The fade time");
        register(module.selfPop, "Applies the chams to yourself");
        register(module.selfColor, "Self color");
        register(module.selfOutline, "Self outline color");
        register(module.copyAnimations, "Copies the player pos");
        register(module.yAnimations, "Y animation");
        register(module.friendPop, "Applies the chams to friends");
        register(module.friendColor, "Friends color");
        register(module.friendOutline, "Friends outline color");

        register(module.particles, "Pop particles");
        register(module.particlesRandom, "Sets the particles color to random");
        register(module.particlesColor, "The particles color");
        register(module.particleFriction, "The particles air friction");
        register(module.particleDuration, "The time duration of the particle");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Adds cool rendering to the popping player";
    }

}
