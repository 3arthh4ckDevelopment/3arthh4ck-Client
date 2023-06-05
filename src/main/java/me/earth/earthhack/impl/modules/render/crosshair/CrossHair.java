package me.earth.earthhack.impl.modules.render.crosshair;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.event.events.render.CrosshairEvent;
import me.earth.earthhack.impl.gui.visibility.PageBuilder;
import me.earth.earthhack.impl.gui.visibility.Visibilities;
import me.earth.earthhack.impl.modules.client.settings.SettingsModule;
import me.earth.earthhack.impl.modules.render.crosshair.mode.GapMode;
import me.earth.earthhack.impl.util.client.SimpleData;

import java.awt.*;

/**
 * @author Gerald
 * @since 6/17/2021
 **/
public class CrossHair extends Module
{
    //TODO: add custom crosshairs?
    protected final Setting<CrossHairPages> pages =
            register(new EnumSetting<>("Page", CrossHairPages.Indicator));
    /* ---------------- CrossHair Settings -------------- */
    protected final Setting<Boolean> crossHair =
            register(new BooleanSetting("Custom-CrossHair", true));
    protected final Setting<Boolean> indicator =
            register(new BooleanSetting("Attack-Indicator", true));
    protected final Setting<Boolean> outline =
            register(new BooleanSetting("Outline", true));
    protected final Setting<Boolean> dot =
            register(new BooleanSetting("Dot", false));
    protected final Setting<Color> dotColor =
            register(new ColorSetting("Dot-Color", new Color(190,60,190)));
    protected final Setting<Float> dotRadius =
            register(new NumberSetting<>("Dot-radius", 1.5f, 0.3f, 5.0f));
    protected final Setting<GapMode> gapMode =
            register(new EnumSetting<>("Gap-Mode", GapMode.Normal));
    protected final Setting<Float> gapSize =
            register(new NumberSetting<>("Gap-Size", 2.0f, 0.5f, 20.0f));

    protected final Setting<Color> color =
            register(new ColorSetting("CrossHair-Color", new Color(190,60,190)));
    protected final Setting<Color> outlineColor =
            register(new ColorSetting("Outline-Color", new Color(0,0,0)));
    protected final Setting<Float> length =
            register(new NumberSetting<>("Length", 5.5f, 0.5f, 50.0f));
    protected final Setting<Float> width =
            register(new NumberSetting<>("Width", 0.5f, 0.1f, 10.0f));
    /* ---------------- PvpInfo Settings -------------- */
    protected final Setting<Boolean> pvpInfo =
            register(new BooleanSetting("PvpInfo", false));
    protected final Setting<Boolean> hudSync =
            register(new BooleanSetting("HudSync", false));
    protected final Setting<Float> scale =
            register(new NumberSetting<>("Scale", 0.8f, 0.0f, 2.0f));
    protected final Setting<Integer> offset =
            register(new NumberSetting<>("TextOffset", 8, 0, 30));
    protected final Setting<Integer> textDistance =
            register(new NumberSetting<>("TextY", 3, 0, 15 ));
    protected final Setting<Boolean> info =
            register(new BooleanSetting("TextInfo", false));
    protected final Setting<Color> PvpInfoColor =
            register(new ColorSetting("InfoColor", new Color(96, 190, 39,80)));

    protected final Setting<Boolean> autoCrystalB =
            register(new BooleanSetting("AutoCrystal", false));
    protected final Setting<Boolean> autoTrapB =
            register(new BooleanSetting("AutoTrap", false));
    protected final Setting<Boolean> blockerB =
            register(new BooleanSetting("Blocker", false));
    protected final Setting<Boolean> holeFillerB =
            register(new BooleanSetting("HoleFiller", false));
    protected final Setting<Boolean> surroundB =
            register(new BooleanSetting("Surround", false));


    public CrossHair()
    {
        super("CrossHair", Category.Render);
        this.listeners.add(new ListenerRender(this));
        this.listeners.add(new EventListener<CrosshairEvent>(CrosshairEvent.class)
        {
            @Override
            public void invoke(CrosshairEvent event)
            {
                event.setCancelled(true);
            }
        });

        SimpleData data = new SimpleData(this, "Gives you a custom crosshair.");
        this.setData(data);

        new PageBuilder<>(this, pages)
                .addPage(p -> p == CrossHairPages.Indicator, crossHair, width)
                .addPage(p -> p == CrossHairPages.PvpInfo, pvpInfo, surroundB)
                .register(Visibilities.VISIBILITY_MANAGER);

        boolean start = false;
        for (Setting<?> setting : this.getSettings()) {
            if (setting == this.pages) {
                start = true;
                continue;
            }

            if (start) {
                Visibilities.VISIBILITY_MANAGER.registerVisibility(
                        setting, Visibilities.orComposer(
                                () -> SettingsModule.COMPLEXITY.getValue()
                                        == Complexity.Beginner));
            }
        }
        this.setData(new CrossHairData(this));
    }

    protected enum CrossHairPages {
        Indicator,
        PvpInfo
    }

}
