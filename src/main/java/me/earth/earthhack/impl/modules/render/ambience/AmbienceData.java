package me.earth.earthhack.impl.modules.render.ambience;

import me.earth.earthhack.api.module.data.DefaultData;
public class AmbienceData extends DefaultData<Ambience> {
    public AmbienceData(Ambience ambience){
        super(ambience);
        register(ambience.color, "Sets a color to apply to the whole world.");
        register(ambience.customFogColor, "If you want to have a custom fog color.");
        register(ambience.fogColor, "Set a custom fog color to use when applying fog modulation.");
        register(ambience.fogDensity, "Set a density for fog modulation. " +
                "\nHigher values mean there will be more fog." +
                "\nLower values mean there will be less fog, and it will be clearer.");
    }
}
