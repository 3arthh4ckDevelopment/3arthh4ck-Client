package me.earth.earthhack.impl.modules.combat.autocrystal.helpers;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.core.ducks.entity.IEntityPlayer;
import me.earth.earthhack.impl.event.events.misc.UpdateEntitiesEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.modules.combat.autocrystal.AutoCrystal;
import me.earth.earthhack.impl.util.math.rotation.RotationUtil;
import me.earth.earthhack.impl.util.minecraft.MotionTracker;
import me.earth.earthhack.impl.util.minecraft.entity.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ExtrapolationHelper extends SubscriberImpl implements Globals {
    private final AutoCrystal module;

    public ExtrapolationHelper(AutoCrystal module) {
        this.module = module;
        this.listeners.add(new LambdaListener<>(UpdateEntitiesEvent.class, e -> {
            for (EntityPlayer player : mc.world.playerEntities) {
                MotionTracker tracker = ((IEntityPlayer) player).getMotionTracker();
                MotionTracker breakTracker = ((IEntityPlayer) player).getBreakMotionTracker();
                if (EntityUtil.isDead(player)
                    || RotationUtil.getRotationPlayer().getDistanceSq(player) > 400
                    || !module.selfExtrapolation.getValue()
                        && player.equals(RotationUtil.getRotationPlayer())) {
                    if (tracker != null) {
                        tracker.active = false;
                    }

                    if (breakTracker != null) {
                        breakTracker.active = false;
                    }

                    continue;
                }

                if (tracker == null) {
                    tracker = new MotionTracker(mc.world, player);
                    ((IEntityPlayer) player).setMotionTracker(tracker);
                }

                if (breakTracker == null) {
                    breakTracker = new MotionTracker(mc.world, player);
                    ((IEntityPlayer) player).setBreakMotionTracker(breakTracker);
                }

                updateTracker(tracker, module.extrapol.getValue());
                updateTracker(breakTracker, module.bExtrapol.getValue());
            }
        }));
    }

    private void updateTracker(MotionTracker tracker, int ticks) {
        tracker.active = false;
        tracker.shouldPushOutOfBlocks = module.pushOutOfBlocks.getValue();
        tracker.shrinkPush = module.shrinkPush.getValue();
        tracker.copyLocationAndAnglesFrom(tracker.tracked);
        tracker.detectWasPhasing();
        for (int i = 0; i < ticks; i++) {
            tracker.updateFromTrackedEntity();
        }

        tracker.active = true;
    }

    public MotionTracker getTrackerFromEntity(Entity player) {
        return ((IEntityPlayer) player).getMotionTracker();
    }

    public MotionTracker getBreakTrackerFromEntity(Entity player) {
        return ((IEntityPlayer) player).getBreakMotionTracker();
    }

}
