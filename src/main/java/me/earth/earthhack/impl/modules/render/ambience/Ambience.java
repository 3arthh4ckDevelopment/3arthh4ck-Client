package me.earth.earthhack.impl.modules.render.ambience;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.core.ducks.entity.IEntityRenderer;
import me.earth.earthhack.impl.util.render.WorldRenderUtil;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.lang.reflect.Field;

public class Ambience extends Module
{

    protected final Setting<Color> color =
            register(new ColorSetting("Color", new Color(255, 255, 255, 255)));
    protected final Setting<Boolean> useSaturation =
            register(new BooleanSetting("UseSaturation", false));
    protected final Setting<Float> saturation =
            register(new NumberSetting<>("Saturation", 0.5f, 0.0f, 1.0f));
    protected final Setting<Boolean> customFogColor =
            register(new BooleanSetting("CustomFogColor", false));
    protected final Setting<Float> fogDensity =
            register(new NumberSetting<>("FogDensity", 0.6f, 0.0f, 1.0f));
    protected final Setting<Color> fogColor =
            register(new ColorSetting("FogColor", new Color(255, 255, 255, 255)));
    protected boolean lightPipeLine;

    public Ambience()
    {
        super("Ambience", Category.Render);
        this.setData(new AmbienceData(this));
        this.color.addObserver(setting -> loadRenderers());
        try
        {
            Field field = Class
                    .forName(ForgeModContainer.class.getName(),
                            true, this.getClass().getClassLoader())
                    .getDeclaredField("forgeLightPipelineEnabled");

            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            this.lightPipeLine = field.getBoolean(null);
            field.setAccessible(accessible);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Color getColor()
    {
        return new Color(color.getValue().getRed(), color.getValue().getGreen(), color.getValue().getBlue(), color.getValue().getAlpha());
    }

    public boolean useSaturation()
    {
        return useSaturation.getValue();
    }
                                                    // why are these unused? hmm
    public float getSaturation()
    {
        return saturation.getValue();
    }

    @Override
    protected void onEnable()
    {
        try
        {
            Field field = Class
                    .forName(ForgeModContainer.class.getName(),
                            true, this.getClass().getClassLoader())
                    .getDeclaredField("forgeLightPipelineEnabled");

            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            this.lightPipeLine = field.getBoolean(null);
            field.set(null, false);
            field.setAccessible(accessible);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        loadRenderers();
        ((IEntityRenderer) mc.entityRenderer).setLightmapUpdateNeeded(true);
    }

    @Override
    public void onDisable()
    {
        try
        {
            Field field = Class
                    .forName(ForgeModContainer.class.getName(),
                            true, this.getClass().getClassLoader())
                    .getDeclaredField("forgeLightPipelineEnabled");

            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(null, this.lightPipeLine);
            field.setAccessible(accessible);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        loadRenderers();
    }
        // refactor world-altering stuff from Management to here, as i feel they belong here more than Management :^)
    @SubscribeEvent
    public void fogModulatorColor(EntityViewRenderEvent.FogColors e){
        if(customFogColor.getValue()){
            e.setRed(fogColor.getValue().getRed());
            e.setGreen(fogColor.getValue().getGreen());
            e.setBlue(fogColor.getValue().getBlue());
        }
    }

    @SubscribeEvent
    public void fogDensity(EntityViewRenderEvent.FogDensity e){
        if(customFogColor.getValue()){
            e.setDensity(fogDensity.getValue());
        }
    }

    public boolean isUsingCustomFogColor()
    {
        return customFogColor.getValue();
    }

    public Color getCustomFogColor()
    {
        return fogColor.getValue();
    }

    public void loadRenderers()
    {
        if (mc.world != null
                && mc.player != null
                && mc.renderGlobal != null
                && mc.gameSettings != null)
        {
            WorldRenderUtil.reload(true);
        }
    }

}
