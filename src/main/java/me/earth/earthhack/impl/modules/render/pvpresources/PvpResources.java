package me.earth.earthhack.impl.modules.render.pvpresources;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;

import java.awt.*;

public class PvpResources extends Module {
    public final Setting<Styles> style =
            register(new EnumSetting<>("Style", Styles.Vertical));
    public final Setting<Boolean> pretty =
            register(new BooleanSetting("Pretty", true));
    public final Setting<Color> color =
            register(new ColorSetting("Color", new Color(0, 0, 0, 0)));
    public final Setting<Float> x =
            register(new NumberSetting<>("X", 0.0F, 0.0f, 1.0f));
    public final Setting<Float> y =
            register(new NumberSetting<>("Y", 0.0F, 0.0f, 1.0f));
    public final Setting<Boolean> obby =
            register(new BooleanSetting("Obsidian", false));

    public PvpResources() {
        super("PvpResources", Category.Render);
        this.listeners.add(new ListenerRender(this));
        this.setData(new PvpResourcesData(this));
    }
}