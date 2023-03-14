package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.*;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.combat.quiver.modes.*;
import me.earth.earthhack.impl.util.client.ModuleUtil;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.earthhack.impl.util.network.NetworkUtil;
import me.earth.earthhack.impl.util.network.PacketUtil;
import me.earth.earthhack.impl.util.text.TextColor;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Quiver extends Module {
    // TODO: Filter tipped arrows like strength and speed separately, so we can shoot both
    // As of now, getArrows is limited to all Tipped Arrows. This should be different.
    protected final Setting<HUDMode> hudMode     =
            register(new EnumSetting<>("HUDMode", HUDMode.Arrows));
    protected final Setting<SwitchMode> switchMode     =
            register(new EnumSetting<>("Switch", SwitchMode.Normal));
    protected final Setting<QuiverMode> quiverMode     =
            register(new EnumSetting<>("Mode", QuiverMode.Automatic));
    protected final Setting<RotationMode> rotateMode     =
            register(new EnumSetting<>("Rotation", RotationMode.Client));
    protected final Setting<Boolean> switchBack =
            register(new BooleanSetting("SwitchBack", true));
    protected final Setting<Integer> delay   =
            register(new NumberSetting<>("Delay", 5, 0, 100));
    protected final Setting<Integer> cyclesAmount   =
            register(new NumberSetting<>("Cycles", 2, 1, 3));
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
    double x, y, z;
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
        if(mc.world == null ||  mc.player == null)
            return;

        shootTime.reset();
        super.onEnable();
        cycles = 0;
        stage = 0;
        hits = 0;
        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
        arrowCount = InventoryUtil.getCount(Items.TIPPED_ARROW);

        yaw   = Managers.ROTATION.getServerYaw();
        pitch = Managers.ROTATION.getServerPitch();

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
                    if(stage == 1)
                        setRotations();
                    // ---------- DRAW BACK ---------- //
                    if(stage == 2)
                    {
                        NetworkUtil.send(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        stage++;
                    }
                    // ---------- SHOOT W/ BOW ---------- //
                    if(stage == 3 && InventoryUtil.isHolding(Items.BOW) && shootTime.passed(100 + delay.getValue()))
                    {

                        NetworkUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));

                        if(cycles < cyclesAmount.getValue())        // If cycles are smaller than CyclesAmount, stage is set back 1
                            stage--;
                        else if(cycles > cyclesAmount.getValue())   // If cycles are somehow bigger than CyclesAmount, it is set back to CyclesAmount
                            cycles = cyclesAmount.getValue();
                        else                                        // If cycles aren't bigger or under than CyclesAmount, it must be equal, so we continue
                            stage++;
                    }
                    else
                    {
                        ModuleUtil.disableRed(this, "Something went wrong!");
                    }
                    // ---------- DISABLE & SWITCHING BACK ---------- //
                    if(stage == 4)
                    {
                        // ROTATION
                            setRotations();
                        // SWITCHING BACK
                        if(switchBack.getValue())
                            InventoryUtil.switchTo(getOldSlot());
                        // CYCLES
                        cycles++;
                        if(cycles > cyclesAmount.getValue())
                            cycles = 0;
                        if(cycles < cyclesAmount.getValue()) // spaghetti?
                            stage = 1;
                        if(cycles == cyclesAmount.getValue())
                            this.disable();
                    }
                    break;
            case Manual:
                // SWITCH
                if(stage == 0)
                {
                    InventoryUtil.switchTo(getSwapSlot());
                    stage++;
                }
                // ROTATE
                if(stage == 1)
                {
                    setRotations();
                    // The rest is for the user!
                }
                break;
        }
    }

    public void setRotations()
    {
        switch(rotateMode.getValue()){
            case Normal:
                if(stage == 1)
                {
                    mc.player.rotationPitch = -90f;
                    mc.player.rotationYaw = yaw;
                }
                else if(stage == 4) // TODO: This, essentially should be a rotation where you don't see you're rotated but you still are, like in AutoCrystal or AntiAim
                {
                    mc.player.rotationPitch = pitch;
                    mc.player.rotationYaw = yaw;
                }

                else return;
            break;

            case Packet: // Honestly idk if this looks like shit, or if it's actually good code lmao.
                if(stage == 1)
                    NetworkUtil.send(new CPacketPlayer.Rotation(yaw, -90.0f, mc.player.onGround));
                else if(stage == 4)
                    NetworkUtil.send(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
                else return;
            break;

            case Client:
                if(stage == 1)
                    mc.player.rotationPitch = -90f; // hmm?
                else if(stage == 4)
                    mc.player.rotationPitch = pitch;
                else return;
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
        switch(hudMode.getValue())
        {
            case Arrows:
                hudmode = String.valueOf(arrowCount);
            break;

            case Hits:
                hudmode = String.valueOf(hits);
            break;

            case None:
                hudmode = null;
            break;
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
    }
}
