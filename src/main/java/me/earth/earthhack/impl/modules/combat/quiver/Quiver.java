package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.modules.combat.quiver.modes.HUDMode;
import me.earth.earthhack.impl.modules.combat.quiver.modes.QuiverMode;
import me.earth.earthhack.impl.modules.combat.quiver.modes.RotationMode;
import me.earth.earthhack.impl.modules.combat.quiver.modes.SwitchMode;
import me.earth.earthhack.impl.util.client.ModuleUtil;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;

import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Quiver extends Module {
    // As of now, getArrows is limited to all Tipped Arrows. This should be different.
    protected final Setting<HUDMode> hudMode     =
            register(new EnumSetting<>("HUDMode", HUDMode.Arrows));
    protected final Setting<SwitchMode> switchMode     =
            register(new EnumSetting<>("Switch", SwitchMode.Normal));
    protected final Setting<QuiverMode> quiverMode     =
            register(new EnumSetting<>("Mode", QuiverMode.Automatic));
    protected final Setting<RotationMode> rotateMode     =
            register(new EnumSetting<>("Rotation", RotationMode.Normal));
    protected final Setting<Boolean> switchBack =
            register(new BooleanSetting("SwitchBack", true));
    protected final Setting<Integer> delay   =
            register(new NumberSetting<>("Delay", 5, 0, 100));
    protected final Setting<Integer> cyclesAmount   =
            register(new NumberSetting<>("Cycles", 2, 1, 3));
    // Cycles should be fixed, since at the moment it doesn't function properly.
    protected final Setting<Boolean> blockedCheck =
            register(new BooleanSetting("BlockedCheck", false));
    protected final Setting<Boolean> mineBlocked =
            register(new BooleanSetting("MineBlocked", false));
    protected final Setting<Boolean> fast =
            register(new BooleanSetting("Fast", false));
    public Quiver(){
        super("Quiver", Category.Combat);
        this.setData(new QuiverData(this));
        this.listeners.add(new ListenerHits(this));
    }

    int swapSlot;
    float yaw;
    float pitch;
    int arrowCount;
    int hits;
    int cycles = 0;
    int stage = 0;
    /* Stage: Because using an enum is kinda annoying to write out when it changes, we should just use an integer.
     * Meaning:
     * 0 - Switch
     * 1 - Rotate
     * 2 - Pulling it back
     * 3 - Shoot
     * 4 - Disable
     *  Lmao I don't think 4 is actually even necessary, but just added it so maybe in the future stage 3 can be made better.
     */
    StopWatch shootTime = new StopWatch();
    String hudmode;

    int oldSlot;
    public void onEnable()
    {
        shootTime.reset();
        super.onEnable();
        cycles = 0;
        stage = 0;
        hits = 0;
        arrowCount = InventoryUtil.getCount(Items.TIPPED_ARROW);

        pitch = mc.player.rotationPitch;
        yaw = mc.player.rotationYawHead;

        if(getSwapSlot() < 0)
        {
            ModuleUtil.disable(this, TextColor.RED + "Disabled, no bow.");
        }
        else
            doQuiver();
        if(mc.player == null)
        {
            ModuleUtil.disable(this, TextColor.RED + "Disabled, not in a world.");
        }
        else
            doQuiver();

    }

    public void doQuiver()
    {
        switch(quiverMode.getValue())
        {
            case Automatic:
                    // ---------- SWITCH ---------- //
                    if (switchMode.getValue() == SwitchMode.Normal && stage == 0)
                    {
                        InventoryUtil.switchTo(getSwapSlot());
                        stage++;
                    }
                    else if(switchMode.getValue() == SwitchMode.FakeSilent)
                    {
                        InventoryUtil.switchToBypass(getSwapSlot());
                        stage++;
                    }

                    // ---------- ROTATIONS ---------- //
                    if(rotateMode.getValue() == RotationMode.Normal)
                    {
                        if(stage == 1)
                        {
                            mc.player.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, yaw, -90);
                            stage++;
                        }
                        else
                            return;
                    }
                    else if(rotateMode.getValue() == RotationMode.Packet)
                    {
                        if(stage == 1)
                        {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, -90, false));
                            // This doesn't work? Will need to fix.
                            stage++;
                        } else
                            return;
                    }
                    // ---------- DRAW BACK ---------- //
                    if(stage == 2)
                    {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        stage++;
                    }
                    // ---------- SHOOT W/ BOW ---------- //
                    if(stage == 3 && InventoryUtil.isHolding(Items.BOW) && shootTime.passed(100 + delay.getValue()))
                    {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                        stage++;
                    }
                    else
                    {
                        ModuleUtil.disableRed(this, "Something went wrong!");
                    }
                    // ---------- DISABLE & SWITCHING BACK ---------- //
                    if(stage == 4)
                    {
                        if(rotateMode.getValue() == RotationMode.Normal)
                            mc.player.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, yaw, pitch);
                        else if(rotateMode.getValue() == RotationMode.Packet)
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, false));
                        if(switchBack.getValue())
                            InventoryUtil.switchTo(getOldSlot());
                        cycles++;
                        if(cycles != cyclesAmount.getValue())
                        {
                            if(cycles > cyclesAmount.getValue())
                                cycles = 0;
                            this.enable();
                        }
                        this.disable();
                    }
                    break;
            case Manual:
                InventoryUtil.switchTo(getSwapSlot()); // This is very primitive...? Should maybe be improved.
                if(rotateMode.getValue().equals(RotationMode.Normal))
                    mc.player.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, yaw, -90);

                else if(rotateMode.getValue().equals(RotationMode.Packet))
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, -90.0f, false));
                break;
        }
    }
    public int getSwapSlot()
    {
        oldSlot = mc.player.inventory.currentItem;
        swapSlot = InventoryUtil.findHotbarItem(Items.BOW);
        return swapSlot;
    }

    public int getOldSlot(){
        return oldSlot;
    }

    /**
     * Hits, Arrows and None. These are the modes, we will give to the player to list in the Arraylist.
     * @return information we should list in the Arraylist.
     */
    public String getDisplayInfo()
    {
        if(hudMode.getValue() == HUDMode.Arrows)
        {
            hudmode = String.valueOf(arrowCount);
        }
        else if(hudMode.getValue() == HUDMode.Hits){
            hudmode = String.valueOf(hits);
        }
        else if (hudMode.getValue() == HUDMode.None){ // spaghetti? lol, should be rewritten.
            hudmode = null;
        }
        return hudmode;
    }

    public void onDisable()
    {
        super.onDisable();
        stage = 0;
        hits = 0;
        shootTime.reset();
        cycles = 0;
        // Rotating back is in the 4th stage of Quiver, but this is a measure to ensure that we are rotating back.
        if(rotateMode.getValue() == RotationMode.Normal)
            mc.player.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, yaw, pitch);
        else if(rotateMode.getValue() == RotationMode.Packet)
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, false));
    }
}
