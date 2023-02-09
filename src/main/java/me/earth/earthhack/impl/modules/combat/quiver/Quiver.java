package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;

public class Quiver extends Module {

    protected final Setting<Integer> delay   =
            register(new NumberSetting<>("Delay", 6, 0, 20));
    protected final Setting<Boolean> tpsSync =
            register(new BooleanSetting("TPS-Sync", false));
    protected final Setting<Boolean> spam =
            register(new BooleanSetting("Spam", true));
    protected final Setting<Boolean> effectArrows =
            register(new BooleanSetting("OnlyEffects", false));

    public Quiver() {
        super("Quiver", Category.Combat);
        this.listeners.add(new ListenerMotion(this));
    }

    public void doQuiver()
    {
        if(effectArrows.getValue())
        {

        }
    }

    @Override
    public String getDisplayInfo()
    {
        // TODO this....
        return null; // this is here as a placeholder :^)
    }

}
