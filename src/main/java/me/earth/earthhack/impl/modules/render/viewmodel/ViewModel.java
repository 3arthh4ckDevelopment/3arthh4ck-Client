package me.earth.earthhack.impl.modules.render.viewmodel;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.visibility.PageBuilder;
import me.earth.earthhack.impl.gui.visibility.Visibilities;
import me.earth.earthhack.impl.util.client.SimpleData;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.lwjgl.opengl.GL11;

/**
 * @author Cubic
 * @since 14.07.2023
 */
public class ViewModel extends Module {

    private final Setting<Mode> mode =
            register(new EnumSetting<>("Mode", Mode.Items));
    public final Setting<Boolean> noSway =
            register(new BooleanSetting("No-Sway", false));

    private final Setting<Page> pages =
            register(new EnumSetting<>("Pages", Page.Translate));
    /* ---------------- Translate Settings -------------- */
    private final Setting<Double> translateMainX =
            register(new NumberSetting<>("TranslateMainX", 0D, -2D, 2D));
    private final Setting<Double> translateMainY =
            register(new NumberSetting<>("TranslateMainY", 0D, -2D, 2D));
    private final Setting<Double> translateMainZ =
            register(new NumberSetting<>("TranslateMainZ", 0D, -3D, 2D));
    private final Setting<Double> translateOffX =
            register(new NumberSetting<>("TranslateOffX", 0D, -2D, 2D));
    private final Setting<Double> translateOffY =
            register(new NumberSetting<>("TranslateOffY", 0D, -3D, 2D));
    private final Setting<Double> translateOffZ =
            register(new NumberSetting<>("TranslateOffZ", 0D, -2D, 2D));
    /* ---------------- Scale Settings -------------- */
    private final Setting<Double> scaleMainX =
            register(new NumberSetting<>("ScaleMainX", 1D, -2D, 2D));
    private final Setting<Double> scaleMainY =
            register(new NumberSetting<>("ScaleMainY", 1D, -2D, 2D));
    private final Setting<Double> scaleMainZ =
            register(new NumberSetting<>("ScaleMainZ", 1D, -2D, 2D));
    private final Setting<Double> scaleOffX =
            register(new NumberSetting<>("ScaleOffX", 1D, -2D, 2D));
    private final Setting<Double> scaleOffY =
            register(new NumberSetting<>("ScaleOffY", 1D, -2D, 2D));
    private final Setting<Double> scaleOffZ =
            register(new NumberSetting<>("ScaleOffZ", 1D, -2D, 2D));
    /* ---------------- Rotate Settings -------------- */
    private final Setting<Double> rotateMainX =
            register(new NumberSetting<>("RotateMainX", 0D, 0D, 360D));
    private final Setting<Double> rotateMainY =
            register(new NumberSetting<>("RotateMainY", 0D, 0D, 360D));
    private final Setting<Double> rotateMainZ =
            register(new NumberSetting<>("RotateMainZ", 0D, 0D, 360D));
    private final Setting<Double> rotateOffX =
            register(new NumberSetting<>("RotateOffX", 0D, 0D, 360D));
    private final Setting<Double> rotateOffY =
            register(new NumberSetting<>("RotateOffY", 0D, 0D, 360D));
    private final Setting<Double> rotateOffZ =
            register(new NumberSetting<>("RotateOffZ", 0D, 0D, 360D));
    /* ---------------- Swing Settings -------------- */
    private final Setting<Double> swingMain =
            register(new NumberSetting<>("SwingMain", 0D, -1D, 1D));
    private final Setting<Double> swingOff =
            register(new NumberSetting<>("SwingOff", 0D, -1D, 1D));


    public ViewModel() {
        super("ViewModel", Category.Render);

        new PageBuilder<>(this, pages)
                .addPage(page -> page == Page.Translate, translateMainX, translateOffZ)
                .addPage(page -> page == Page.Scale, scaleMainX, scaleOffZ)
                .addPage(page -> page == Page.Rotate, rotateMainX, rotateOffZ)
                .addPage(page -> page == Page.Swing, swingMain, swingOff)
                .register(Visibilities.VISIBILITY_MANAGER);
        SimpleData data = new SimpleData(this, "Changes how the held items look");
        data.register(noSway, "Changes how the held items look");
        this.setData(data);
    }

    public void doTransform(EnumHandSide side) {
        if (side == EnumHandSide.LEFT) {
            GL11.glScaled(scaleOffX.getValue(), scaleOffY.getValue(), scaleOffZ.getValue());
            GL11.glRotated(rotateOffX.getValue(), 1, 0, 0);
            GL11.glRotated(rotateOffY.getValue(), 0, 1, 0);
            GL11.glRotated(rotateOffZ.getValue(), 0, 0, 1);
            GL11.glTranslated(translateOffX.getValue(), translateOffY.getValue(), translateOffZ.getValue());
            return;
        }
        GL11.glScaled(scaleMainX.getValue(), scaleMainY.getValue(), scaleMainZ.getValue());
        GL11.glRotated(rotateMainX.getValue(), 1, 0, 0);
        GL11.glRotated(rotateMainY.getValue(), 0, 1, 0);
        GL11.glRotated(rotateMainZ.getValue(), 0, 0, 1);
        GL11.glTranslated(translateMainX.getValue(), translateMainY.getValue(), translateMainZ.getValue());
    }

    public float getSwing(EnumHand hand) {
        return hand == EnumHand.MAIN_HAND ? swingMain.getValue().floatValue() : swingOff.getValue().floatValue();
    }

    public boolean isItems() {
        return mode.getValue() == Mode.Items;
    }

    public boolean isHand() {
        return mode.getValue() == Mode.Hands;
    }

    private enum Page {
        Translate,
        Scale,
        Rotate,
        Swing
    }

    private enum Mode {
        Items,
        Hands,
    }

}
