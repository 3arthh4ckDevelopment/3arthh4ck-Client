package me.earth.earthhack.impl.modules.render.popchams;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.visibility.PageBuilder;
import me.earth.earthhack.impl.gui.visibility.Visibilities;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.client.hud.HUDData;
import me.earth.earthhack.impl.modules.client.settings.SettingsModule;
import me.earth.earthhack.impl.modules.render.popchams.util.PopChamsPages;
import me.earth.earthhack.impl.util.helpers.render.BlockESPBuilder;
import me.earth.earthhack.impl.util.helpers.render.IAxisESP;
import me.earth.earthhack.impl.util.render.entity.StaticModelPlayer;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PopChams extends Module
{
    protected final Setting<PopChamsPages> pages =
            register(new EnumSetting<>("Page", PopChamsPages.Player));
    /* ---------------- Player Settings -------------- */
    protected final ColorSetting color =
            register(new ColorSetting("Color", new Color(255, 45, 45, 80)));
    protected final ColorSetting outline =
            register(new ColorSetting("Outline", new Color(255, 45, 45, 255)));
    protected final Setting<Float> lineWidth =
            register(new NumberSetting<>("LineWidth", 1.5f, 0.0f, 10.0f));
    protected final Setting<Integer> fadeTime =
            register(new NumberSetting<>("Fade-Time", 1500, 0, 5000));
    protected final Setting<Boolean> selfPop =
            register(new BooleanSetting("Self-Pop", false));
    public final Setting<Color> selfColor =
            register(new ColorSetting("Self-Color", new Color(80, 80, 255, 80)));
    public final Setting<Color> selfOutline =
            register(new ColorSetting("Self-Outline", new Color(80, 80, 255, 255)));
    public final Setting<Boolean> copyAnimations =
            register(new BooleanSetting("Copy-Animations", true));
    public final Setting<Double> yAnimations =
            register(new NumberSetting<>("Y-Animation", 0., -7., 7.));
    protected final Setting<Boolean> friendPop =
            register(new BooleanSetting("Friend-Pop", false));
    public final Setting<Color> friendColor =
            register(new ColorSetting("Friend-Color", new Color(45, 255, 45, 80)));
    public final Setting<Color> friendOutline =
            register(new ColorSetting("Friend-Outline", new Color(45, 255, 45, 255)));

    /* ---------------- Particles Settings -------------- */
    public final Setting<Boolean> particles =
            register(new BooleanSetting("Custom", false));
    public final Setting<Boolean> particlesRandom =
            register(new BooleanSetting("RandomColor", true));
    public final Setting<Color> particlesColor =
            register(new ColorSetting("ParticleColor", new Color(249, 255, 0)));
    public final Setting<Float> particleFriction =
            register(new NumberSetting<>("AirFriction", 0.6f, 0.1f, 1.0f)); // your game will crash if you go over this
    public final Setting<Double> particleDuration =
            register(new NumberSetting<>("Duration", 1.0, 0.0, 2.0));

    private final List<PopData> popDataList = new ArrayList<>();

    protected IAxisESP esp = new BlockESPBuilder()
            .withColor(color)
            .withOutlineColor(outline)
            .withLineWidth(lineWidth)
            .build();

    public PopChams()
    {
        super("PopChams", Category.Render);
        this.listeners.add(new ListenerRender(this));
        this.listeners.add(new ListenerTotemPop(this));
        this.setData(new PopChamsData(this));

        new PageBuilder<>(this, pages)
                .addPage(p -> p == PopChamsPages.Player, fadeTime, friendOutline)
                .addPage(p -> p == PopChamsPages.Particles, particles, particleDuration)
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
    }

    public List<PopData> getPopDataList() {
        return popDataList;
    }

    protected Color getColor(EntityPlayer entity) {
        if (entity.equals(mc.player)) {
            return selfColor.getValue();
        } else if (Managers.FRIENDS.contains(entity)) {
            return friendColor.getValue();
        } else {
            return color.getValue();
        }
    }

    protected Color getOutlineColor(EntityPlayer entity) {
        if (entity.equals(mc.player)) {
            return selfOutline.getValue();
        } else if (Managers.FRIENDS.contains(entity)) {
            return friendOutline.getValue();
        } else {
            return outline.getValue();
        }
    }

    protected boolean isValidEntity(EntityPlayer entity) {
        return !(entity == mc.player && !selfPop.getValue()) && !((Managers.FRIENDS.contains(entity) && entity != mc.player) && !friendPop.getValue());
    }

    public static class PopData {
        private final EntityPlayer player;
        private final StaticModelPlayer model;
        private final long time;
        private final double x;
        private final double y;
        private final double z;

        public PopData(EntityPlayer player, long time, double x, double y, double z, boolean slim) {
            this.player = player;
            this.time = time;
            this.x = x;
            this.y = y - (player.isSneaking() ? 0.125 : 0);
            this.z = z;
            this.model = new StaticModelPlayer(player, slim, 0);
            this.model.disableArmorLayers();
        }

        public EntityPlayer getPlayer() {
            return player;
        }

        public long getTime() {
            return time;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public StaticModelPlayer getModel() {
            return model;
        }
    }
}
