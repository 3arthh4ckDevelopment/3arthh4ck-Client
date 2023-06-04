package me.earth.earthhack.impl.modules.client.colors;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.impl.managers.Managers;

import java.awt.*;

import static me.earth.earthhack.impl.gui.click.Click.CLICK_GUI;

public class Colors extends Module
{
    //TODO: find gui bg and gui side
    protected final Setting<Boolean> extended =
            register(new BooleanSetting("ExtendedColors", false));
    public Setting<Color> modulesColorsetting =
            register(new ColorSetting("ModulesColor", new Color(0, 80, 255, 255)));
    public Setting<Color> moduleHoversetting =
            register(new ColorSetting("ModuleHover", new Color(0, 80, 255, 255)));
    public Setting<Color> settingColorsetting =
            register(new ColorSetting("SettingColor", new Color(224, 224, 224, 255)));
    public Setting<Color> onModulesetting =
            register(new ColorSetting("EnabledText", new Color(0, 80, 255, 255)));
    public Setting<Color> offModulesetting =
            register(new ColorSetting("DisabledText", new Color(0, 80, 255, 255)));
    public Setting<Color> topColorsetting =
            register(new ColorSetting("SectionColorSide", new Color(0, 80, 255, 255)));
    public Setting<Color> topBgColorsetting =
            register(new ColorSetting("SectionBackGround", new Color(0, 0, 0, 255)));
    public Setting<Color> textColorDescsetting =
            register(new ColorSetting("DescriptionColor", new Color(224, 224, 224, 255)));
    /*
    public final ColorSetting bgColor =
            register(new ColorSetting("BackGround", new Color(0, 0, 0, 126))); // nope
    public final ColorSetting sidesColor =
            register(new ColorSetting("SidesColor", new Color(0, 0, 0, 255))); // nope
     */
    public Setting<Color> catEarsSetting =
            register(new ColorSetting("CatEars", new Color(255, 0, 234, 255)));

    public Color modulesColor = modulesColorsetting.getValue();
    public Color moduleHover = moduleHoversetting.getValue();
    public Color settingColor = settingColorsetting.getValue();
    public Color onModule = onModulesetting.getValue();
    public Color offModule = offModulesetting.getValue();
    public Color topColor = topColorsetting.getValue();
    public Color topBgColor = topBgColorsetting.getValue();
    public Color textColorDesc = textColorDescsetting.getValue();
    public Color catEars = catEarsSetting.getValue();


    public Colors()
    {
        super("Colors", Category.Client);
        register(Managers.COLOR.getColorSetting());
        register(Managers.COLOR.getRainbowSpeed());
        this.setData(new ColorsData(this));
        Bus.EVENT_BUS.register(new ListenerTick(this));
    }

    public void updateColors() {
        if (!extended.getValue()) {
            modulesColor = CLICK_GUI.get().color.getValue();
            moduleHover = new Color(70,70,70,70);
            onModule = new Color(220, 220, 220,255);
            offModule = new Color(120, 120, 120,255);
            topColor = new Color(0,0,0,255);
            topBgColor = CLICK_GUI.get().color.getValue();
            textColorDesc = new Color(220, 220, 220,255);
            catEars = CLICK_GUI.get().color.getValue();
        } else {
            modulesColor = modulesColorsetting.getValue();
            moduleHover = moduleHoversetting.getValue();
            onModule = onModulesetting.getValue();
            offModule = offModulesetting.getValue();
            topColor = topColorsetting.getValue();
            topBgColor = topBgColorsetting.getValue();
            textColorDesc = textColorDescsetting.getValue();
            catEars = catEarsSetting.getValue();
        }
    }

    public Color getModulesColor() {
        updateColors();
        return modulesColor;
    }

    public Color getModuleHover() {
        updateColors();
        return moduleHover;
    }

    public Color getSettingColor() {
        updateColors();
        return settingColor;
    }

    public Color getOnModule() {
        updateColors();
        return onModule;
    }

    public Color getOffModule() {
        updateColors();
        return offModule;
    }

    public Color getTopColor() {
        updateColors();
        return topColor;
    }

    public Color getTopBgColor() {
        updateColors();
        return topBgColor;
    }

    public Color getTextColorDesc() {
        updateColors();
        return textColorDesc;
    }

    public Color getCatEars() {
        updateColors();
        return catEars;
    }
}

