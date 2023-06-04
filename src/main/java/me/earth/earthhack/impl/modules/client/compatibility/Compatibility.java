package me.earth.earthhack.impl.modules.client.compatibility;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;

public class Compatibility extends Module {
    protected final Setting<Boolean> showRotations = register(
        new BooleanSetting("ShowRotations", false));
    protected final Setting<Boolean> swiftSneak = register(
            new BooleanSetting("SwiftSneak3", false));

    public Compatibility() {
        super("Compatibility", Category.Client);
        this.listeners.add(new ListenerMotion(this));
        this.setData(new CompatibilityData(this));
    }

    public boolean isShowingRotations() {
        return showRotations.getValue();
    }

}
