package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;

import java.awt.*;

public class ChorusControl extends Module {
    //TODO rewrite... Should be happening soon.
    protected final Setting<Boolean> esp =
            register(new BooleanSetting("ESP", true));
    protected final Setting<EspMode> espMode =
            register(new EnumSetting<>("ESPMode", EspMode.Box));
    protected final Setting<Boolean> onlySneak =
            register(new BooleanSetting("OnlySneak", false));
    public final ColorSetting espColor =
            register(new ColorSetting("Color", new Color(255, 255, 255, 240)));

    int x, y, z;
    boolean startCancel;
    boolean sendingPackets;
    boolean eatingChorus;
    public ChorusControl() {
        super("ChorusControl", Category.Misc);
        this.setData(new ChorusControlData(this));
        this.listeners.add(new ListenerEatChorus(this));
        this.listeners.add(new ListenerPacket(this));
    }

    protected void onEnable(){

        if(mc.world == null ||  mc.player == null)
            return;

        startCancel = sendingPackets = false;
        x = (int) mc.player.posX;
        y = (int) mc.player.posY;
        z = (int) mc.player.posZ;
    }

    public enum EspMode { // this should have more settings imo
        Box,
        Pillar
    }
}
