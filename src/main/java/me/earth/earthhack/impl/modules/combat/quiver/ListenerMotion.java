package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.event.events.Stage;
import me.earth.earthhack.impl.event.events.network.MotionUpdateEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.thread.Locks;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

final class ListenerMotion extends ModuleListener<Quiver, MotionUpdateEvent> {
    private float lastTimer = -1.f;

    public ListenerMotion(Quiver module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void invoke(MotionUpdateEvent event) {
        if (event.getStage() == Stage.POST) {
            ItemStack stack = getStack();
            if (module.spam.getValue()) {
                if (mc.player.onGround) {
                    if (stack != null
                            && !mc.player.getActiveItemStack().isEmpty()
                            && mc.player.getItemInUseCount() > 0) {

                        Managers.TIMER.setTimer(6.0f);

                        if (stack.getMaxItemUseDuration() - mc.player.getItemInUseCount() > (module.delay.getValue() * 6)) {
                            Locks.acquire(Locks.PLACE_SWITCH_LOCK, () ->
                                    mc.playerController.onStoppedUsingItem(mc.player));
                        }
                    } else {
                        if (lastTimer > 0 && Managers.TIMER.getSpeed() != lastTimer) {
                            Managers.TIMER.setTimer(lastTimer);
                        }
                        lastTimer = Managers.TIMER.getSpeed();
                    }
                }
            } else {
                if (lastTimer > 0 && Managers.TIMER.getSpeed() != lastTimer) {
                    Managers.TIMER.setTimer(lastTimer);
                    lastTimer = 1.f;
                }
                if (stack != null // check if stack.equals(mc.player.getActive...)?
                        && !mc.player.getActiveItemStack().isEmpty()
                        && (stack.getMaxItemUseDuration()
                        - mc.player.getItemInUseCount())
                        - (module.tpsSync.getValue()
                        ? 20.0f - Managers.TPS.getTps()
                        : 0.0f) >= module.delay.getValue()) {

                    Locks.acquire(Locks.PLACE_SWITCH_LOCK, () ->
                            mc.playerController.onStoppedUsingItem(mc.player));
                }
            }
        }
    }

    private ItemStack getStack() {
        ItemStack mainHand = mc.player.getHeldItemMainhand();

        if (mainHand.getItem() instanceof ItemBow) {
            return mainHand;
        }

        ItemStack offHand = mc.player.getHeldItemOffhand();

        if (offHand.getItem() instanceof ItemBow) {
            return offHand;
        }

        return null;
    }

}
