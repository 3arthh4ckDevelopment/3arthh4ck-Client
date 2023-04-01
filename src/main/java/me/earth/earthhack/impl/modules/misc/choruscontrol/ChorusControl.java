package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;

import java.awt.*;

public class ChorusControl extends Module {

    // When rewrite is finished, this would close #229 from origin.

    protected final Setting<Boolean> esp =
            register(new BooleanSetting("ESP", true));
    public final ColorSetting espColor =
            register(new ColorSetting("Color", new Color(255, 255, 255, 240)));
    public final Setting<Boolean> onlySneak =
            register(new BooleanSetting("OnlySneak", false));
    protected final Setting<EspMode> espMode =
            register(new EnumSetting<>("ESPMode", EspMode.Box));

    boolean valid;
    boolean cancelled;
    double x, y, z;
    public ChorusControl() {
        super("ChorusControl", Category.Misc);
        this.setData(new ChorusControlData(this));
        this.listeners.add(new ListenerSPacket(this));
        this.listeners.add(new ListenerChorus(this));
        this.listeners.add(new ListenerRender(this));
    }

    protected void onEnable(){
        super.onEnable();

        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
    }

    protected void onDisable(){
        super.onDisable();



        valid = false;
        cancelled = false;
    }

    public enum EspMode {
        Box
        // More of these?
    }
}
