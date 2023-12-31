package me.earth.earthhack.impl.modules.misc.choruscontrol;

import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.modules.misc.choruscontrol.util.ESPMode;
import me.earth.earthhack.impl.modules.misc.choruscontrol.util.HUDMode;
import me.earth.earthhack.impl.util.helpers.render.BlockESPModule;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.render.entity.StaticModelPlayer;

public class ChorusControl extends BlockESPModule {
    protected final Setting<HUDMode> hudMode =
            register(new EnumSetting<>("HUD-Mode", HUDMode.None));
    protected final Setting<Integer> autoOffDelay =
            register(new NumberSetting<>("Off-Delay", 0, 0, 5000))
                    .setComplexity(Complexity.Medium);
    protected final Setting<Boolean> esp =
            register(new BooleanSetting("ESP", true))
                    .setComplexity(Complexity.Medium);
    // TODO: Tracer?
    protected final Setting<ESPMode> espMode =
            register(new EnumSetting<>("ESPMode", ESPMode.Chams));
    protected final Setting<Integer> pulseCycle =
            register(new NumberSetting<>("PulseTime", 0, 0, 6000))
                    .setComplexity(Complexity.Medium);
    public final Setting<Double> yAnimations =
            register(new NumberSetting<>("Y-Animation", 0., -7., 7.))
                    .setComplexity(Complexity.Expert);

    protected final StopWatch autoOffTimer = new StopWatch();
    boolean cancelled;
    boolean cached;
    double tpX, tpY, tpZ;
    long time;
    StaticModelPlayer model;
    public ChorusControl() {
        super("ChorusControl", Category.Misc);
        this.setData(new ChorusControlData(this));
        this.listeners.add(new ListenerSPacket(this));
        this.listeners.add(new ListenerRender(this));
        this.listeners.add(new ListenerEat(this));
    }

    public String getDisplayInfo(){
        if(hudMode.getValue() == HUDMode.Info){
            return cancelled
                    ? "Aborted"
                    : "Standby";
        }else
            return null;
    }

    protected void onEnable(){
        super.onEnable();
    }

    protected void onDisable(){
        super.onDisable();
        if(cancelled && (mc.player.posX != tpX || mc.player.posY != tpY || mc.player.posZ != tpZ))
            mc.player.setPosition(tpX, tpY, tpZ);

        // tpX = tpY = tpZ = Double.NaN;
        cancelled = false;
        cached = false;
        autoOffTimer.reset();
    }
}
