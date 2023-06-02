package me.earth.earthhack.impl.modules.movement.clip;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.event.events.misc.UpdateEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.modules.misc.logger.Logger;
import me.earth.earthhack.impl.util.minecraft.MovementUtil;
import me.earth.earthhack.impl.util.network.NetworkUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;


public class Clip extends Module {

    protected final Setting<ClipMode> mode   =
            register(new EnumSetting<>("Mode", ClipMode.Corner));

    protected final Setting<Integer> delay =
            register(new NumberSetting<>("Delay", 5, 1, 10));

    protected final Setting<Boolean> disable =
            register(new BooleanSetting("Disable", false));

    protected final Setting<Integer> updates =
            register(new NumberSetting<>("Updates", 10, 1, 30));


    public BlockPos pos;
    int disableTime = 0;

    public Clip()
    {
        super("Clip", Category.Movement);
        this.setData(new ClipData(this));
        this.listeners.add(new LambdaListener<>(UpdateEvent.class, e -> {
            if (mc.world == null || mc.player == null) {
                return;
            }
            if (!MovementUtil.noMovementKeys()) {
                disable();
                return;
            }
            switch (mode.getValue()) {

                case AutoCenter:
                    try
                    {
                        Vec3d setCenter = new Vec3d(pos.getX() + 0.5, mc.player.posY, pos.getZ() + 0.5);

                        mc.player.motionX = 0;
                        mc.player.motionZ = 0;

                        NetworkUtil.send(new CPacketPlayer.Position(setCenter.x, setCenter.y, setCenter.z, true));
                        mc.player.setPosition(setCenter.x, setCenter.y, setCenter.z);
                    } catch (Exception e2) {
                        System.out.println(2);
                    }
                    break;

                case Corner:
                    if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().grow(0.01, 0, 0.01)).size() < 2) {
                        mc.player.setPosition(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.301, Math.floor(mc.player.posX) + 0.699), mc.player.posY, roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.301, Math.floor(mc.player.posZ) + 0.699));

                    } else if (mc.player.ticksExisted % delay.getValue() == 0) {
                        mc.player.setPosition(mc.player.posX + MathHelper.clamp(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.241, Math.floor(mc.player.posX) + 0.759) - mc.player.posX, -0.03, 0.03), mc.player.posY, mc.player.posZ + MathHelper.clamp(roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.241, Math.floor(mc.player.posZ) + 0.759) - mc.player.posZ, -0.03, 0.03));
                        NetworkUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                        NetworkUtil.send(new CPacketPlayer.Position(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.23, Math.floor(mc.player.posX) + 0.77), mc.player.posY, roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.23, Math.floor(mc.player.posZ) + 0.77), true));
                    }
                    break;

                case FiveB:
                    NetworkUtil.send(new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 0.0042123,mc.player.posZ,mc.player.onGround));
                    NetworkUtil.send(new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 0.02141,mc.player.posZ,mc.player.onGround));
                    NetworkUtil.send(new CPacketPlayer.PositionRotation(mc.player.posX,mc.player.posY - 0.097421,mc.player.posZ,500,500,mc.player.onGround));
                    // https://github.com/WMSGaming/Abstract-1.12.2/blob/master/src/main/java/com/wms/abstractclient/module/modules/exploit/CornerClip.java
                    break;
            }

            disableTime++;
            if (disable.getValue()) {
                if (disableTime >= updates.getValue()) {
                    disable();
                }
            }
        }));
    }

    public static double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        return (d2 > d1 ? low : high);
    }
    
    @Override
    protected void onDisable()
    {
        disableTime = 0;
    }
}