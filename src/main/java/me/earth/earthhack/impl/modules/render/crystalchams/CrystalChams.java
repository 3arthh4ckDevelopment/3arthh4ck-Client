package me.earth.earthhack.impl.modules.render.crystalchams;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.visibility.PageBuilder;
import me.earth.earthhack.impl.gui.visibility.Visibilities;
import me.earth.earthhack.impl.modules.render.handchams.modes.ChamsMode;
import me.earth.earthhack.impl.util.animation.TimeAnimation;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrystalChams extends Module {

    private final Setting<Page> pages =
            register(new EnumSetting<>("Page", Page.Chams));

    /* Chams */
    public final Setting<ChamsMode> mode =
            register(new EnumSetting<>("Mode", ChamsMode.Normal));
    public final Setting<Boolean> chams =
            register(new BooleanSetting("Chams", false));
    public final Setting<Boolean> throughWalls =
            register(new BooleanSetting("ThroughWalls", false));
    public final Setting<Boolean> wireframe =
            register(new BooleanSetting("Wireframe", false));
    public final Setting<Boolean> wireWalls =
            register(new BooleanSetting("WireThroughWalls", false));
    public final Setting<Boolean> texture =
            register(new BooleanSetting("Texture", false));
    public final Setting<Float> lineWidth =
            register(new NumberSetting<>("LineWidth" , 1f , 0.1f , 4f));
    public final Setting<Color> color =
            register(new ColorSetting("Color", new Color(255, 255, 255, 255)));
    public final Setting<Color> wireFrameColor =
            register(new ColorSetting("WireframeColor", new Color(255, 255, 255, 255)));

    /* Scale */

    public final Setting<Float> scale =
            register(new NumberSetting<>("Scale", 1.0f, 0.1f, 2.0f));
    public final Setting<Boolean> animate =
            register(new BooleanSetting("Animate", false));
    public final Setting<Integer> time =
            register(new NumberSetting<>("AnimationTime", 200, 1, 500));
    public final Setting<Float> spinSpeed =
            register(new NumberSetting<>("Spin-Speed", 1.0f, 0.0f, 5.0f));
    public final Setting<Float> bounceFactor =
            register(new NumberSetting<>("Bounce-Factor", 1.0f, 0.0f, 3.0f));


    public final Map<Integer, TimeAnimation> scaleMap =
            new ConcurrentHashMap<>();


    public CrystalChams() {
        super("CrystalChams", Category.Render);

        this.listeners.add(new ListenerDestroyEntities(this));
        this.listeners.add(new ListenerSpawnObject(this));
        this.listeners.add(new ListenerCrystalRender(this));
        this.setData(new CrystalChamsData(this));

        new PageBuilder<>(this, pages)
                .addPage(p -> p == Page.Chams, mode, wireFrameColor)
                .addPage(p -> p == Page.Scale, scale, bounceFactor)
                .register(Visibilities.VISIBILITY_MANAGER);
    }

    private enum Page {
        Chams,
        Scale

    }

}
