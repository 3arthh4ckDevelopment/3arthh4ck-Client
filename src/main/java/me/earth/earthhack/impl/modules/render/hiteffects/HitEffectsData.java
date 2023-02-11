package me.earth.earthhack.impl.modules.render.hiteffects;

import me.earth.earthhack.api.module.data.DefaultData;

public class HitEffectsData extends DefaultData<HitEffects> {
    public HitEffectsData(HitEffects module){
        super(module);
        register(module.lightning, "Strikes lightning when you hit an enemy.");
        register(module.screenShader, "Applies a blur and some bloom to your screen after killing"
                + " an enemy.");
        register(module.screenShaderLength, "How long the ScreenShader should be rendered.");
        register(module.onlyOnKill, "Only renders effects when you kill an enemy.");
        register(module.superheroFx, "Renders effects and text like 'kaboom', 'pow' and 'zap'"
                + "when you hit an enemy.");
        register(module.superheroFadeTime, "How long SuperheroFX texts should exist in seconds.");
        register(module.onlyTargets, "Only renders effects on targets");
    }
    public String getDescription(){
        return "Renders different effects when you damage enemies.";
    }
}
