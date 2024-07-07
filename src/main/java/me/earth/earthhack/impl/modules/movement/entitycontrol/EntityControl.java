package me.earth.earthhack.impl.modules.movement.entitycontrol;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.visibility.PageBuilder;
import me.earth.earthhack.impl.gui.visibility.Visibilities;
import me.earth.earthhack.impl.util.math.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class EntityControl extends Module {

    private final Setting<Page> page =
            register(new EnumSetting<>("Page", Page.Control));

    /* ---------------- Control -------------- */
    protected final Setting<Boolean> control =
            register(new BooleanSetting("Control", true));
    protected final Setting<Double> jumpHeight =
            register(new NumberSetting<>("JumpHeight", 0.7, 0.0, 2.0));
    protected final Setting<Boolean> noAI =
            register(new BooleanSetting("NoAI", true));

    /* ---------------- Speed -------------- */
    protected final Setting<Boolean> speedSetting =
            register(new BooleanSetting("Speed", false));
    protected final Setting<Float> speed =
            register(new NumberSetting<>("Speed", 3.8f, 0.1f, 10.0f));
    protected final Setting<Boolean> noStuck =
            register(new BooleanSetting("NoStuck", false));
    protected final Setting<Boolean> resetStuck =
            register(new BooleanSetting("Reset-Stuck", false));
    protected final Setting<Integer> stuckTime =
            register(new NumberSetting<>("Stuck-Time", 10000, 0, 10000));

    protected final StopWatch stuckTimer = new StopWatch();
    protected final StopWatch jumpTimer  = new StopWatch();
    protected List<BlockPos> positions = new ArrayList<>();

    public EntityControl() {
        super("EntityControl", Category.Movement);
        this.listeners.add(new ListenerControl(this));
        this.listeners.add(new ListenerAI(this));
        this.listeners.add(new ListenerHorse(this));
        this.listeners.add(new ListenerTick(this));
        this.setData(new EntityControlData(this));

        new PageBuilder<>(this, page)
                .addPage(p -> p == Page.Control, control, noAI)
                .addPage(p -> p == Page.Speed, speedSetting, stuckTime)
                .register(Visibilities.VISIBILITY_MANAGER);
    }

    @Override
    public String getDisplayInfo()
    {
        return speed.getValue() + "";
    }

    /**
     * Moves {@link Minecraft#player}s ridden entity.
     *
     * @param speed the speed to move with.
     */
    public static void strafe(double speed)
    {
        MovementInput input = mc.player.movementInput;
        double forward = input.moveForward;
        double strafe  = input.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0)
        {
            //noinspection ConstantConditions
            mc.player.getRidingEntity().motionX = 0.0;
            mc.player.getRidingEntity().motionZ = 0.0;
            return;
        }

        if (forward != 0.0)
        {
            if (strafe > 0.0)
            {
                yaw += forward > 0.0 ? -45 : 45;
            }
            else if (strafe < 0.0)
            {
                yaw += forward > 0.0 ? 45 : -45;
            }

            strafe = 0.0;
            if (forward > 0.0)
            {
                forward = 1.0;
            }
            else if (forward < 0.0)
            {
                forward = -1.0;
            }
        }

        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        //noinspection ConstantConditions
        mc.player.getRidingEntity().motionX =
                forward * speed * cos + strafe * speed * sin;
        mc.player.getRidingEntity().motionZ =
                forward * speed * sin - strafe * speed * cos;
    }

    private enum Page {
        Control,
        Speed
    }

}
