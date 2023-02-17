package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.*;
import me.earth.earthhack.impl.modules.combat.quiver.modes.*;
import me.earth.earthhack.impl.util.client.ModuleUtil;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Quiver extends Module {
    //TODO: FromInv - to get a bow from player inventory, place in hotbar and then quiver, also should be improved later
    protected final Setting<HUDMode> hudMode     =
            register(new EnumSetting<>("HUDMode", HUDMode.Arrows));
    protected final Setting<SwitchMode> switchMode     =
            register(new EnumSetting<>("Switch", SwitchMode.Normal));
    protected final Setting<QuiverMode> quiverMode     =
            register(new EnumSetting<>("Mode", QuiverMode.Automatic));
    protected final Setting<RotationMode> rotateMode     =
            register(new EnumSetting<>("Rotation", RotationMode.Normal));
    protected final Setting<Integer> delay   =
            register(new NumberSetting<>("Delay", 5, 0, 20));
    protected final Setting<Boolean> tpsSync =
            register(new BooleanSetting("TPS-Sync", false));
    protected final Setting<Boolean> blockedCheck =
            register(new BooleanSetting("BlockedCheck", false));
    protected final Setting<Boolean> mineBlocked =
            register(new BooleanSetting("MineBlocked", false));
    protected final Setting<Boolean> fast =
            register(new BooleanSetting("Fast", true));

    public Quiver() {
        super("Quiver", Category.Combat);
        this.setData(new QuiverData(this));
        this.listeners.add(new ListenerMotion(this));
    }

    // stuff we will use for quiver
    boolean hasBow;
    int arrowHits = 0;
    int arrowCount;
    float currentPitch;
    float currentYaw;

    public void doQuiver()
    {
        if(mc.player == null)
        {
            ModuleUtil.disable(this, TextColor.RED + "Disabled, no player.");
        }else if(!hasBow() || arrowCount <= 0)
            ModuleUtil.disable(this,TextColor.RED + "Disabled, no bow or arrows.");
        else
        {
            switch(rotateMode.getName()){
                case "Normal":
                    if(quiverMode.getName().equals("Automatic"))
                    {
                        mc.player.rotationPitch = -90.0f;
                        if(!hasBow())
                        {
                            ModuleUtil.disable(this, TextColor.RED + "Disabled, no bow.");
                            mc.player.rotationPitch = currentPitch;
                            mc.player.rotationYaw = currentYaw;
                        }

                        if(switchMode.getName().equals("Normal") && hasBow())
                        {
                            InventoryUtil.switchTo(InventoryUtil.findHotbarItem(Items.BOW));
                                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                                mc.player.stopActiveHand();
                                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                                mc.player.setActiveHand(EnumHand.MAIN_HAND);
                        }
                        else if(switchMode.getName().equals("Silent") && hasBow()) // spaghetti? :D
                        {
                            InventoryUtil.bypassSwitch(InventoryUtil.findHotbarItem(Items.BOW));
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                            mc.player.stopActiveHand();
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            mc.player.setActiveHand(EnumHand.MAIN_HAND);
                        }
                        else
                        {
                            ModuleUtil.disable(this, TextColor.RED + "Disabled, no bow.");
                        }
                    }
                    else
                    {
                        mc.player.rotationPitch = -90.0f;
                        ModuleUtil.sendMessage(this, TextColor.GREEN + "You can now shoot yourself with arrows.");
                    }
                    break;
                case "Packet":
                    if(quiverMode.getName().equals("Automatic"))
                    {
                        if(!hasBow())
                        {
                            ModuleUtil.disable(this, TextColor.RED + "Disabled, no bow.");
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(currentYaw, currentPitch, true));
                        }
                        else if(hasBow && arrowCount > 0)
                        {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(currentYaw, -90.0f, true));
                            if(switchMode.getName().equals("Normal") && hasBow())
                            {
                                InventoryUtil.switchTo(InventoryUtil.findHotbarItem(Items.BOW));
                            }
                            else if(switchMode.getName().equals("Silent") && hasBow())
                            {
                                InventoryUtil.bypassSwitch(InventoryUtil.findHotbarItem(Items.BOW));
                                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            }
                            else
                            {
                                ModuleUtil.disable(this, TextColor.RED + "Disabled, no bow.");
                            }
                        }
                    }
                    else
                    {
                        if(hasBow() && arrowCount > 0)
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(currentYaw, -90.0f, true));
                        else
                            ModuleUtil.disable(this, TextColor.RED + "Disabled, no bow or arrows.");
                    }
                break;
            }
        }
    }

    public boolean hasBow(){
        hasBow = InventoryUtil.getCount(Items.BOW) > 0;
        return hasBow;
    }
    public int getArrowCount(){
        arrowCount = InventoryUtil.getCount(Items.TIPPED_ARROW);
        return arrowCount;
    }

    public void onEnable()
    {
        super.onEnable();
        if(mc.player != null)
        {
            currentPitch = mc.player.cameraPitch; // this might cause problems?
            currentYaw = mc.player.cameraYaw;     // this might cause problems? // TODO: improve this lmao
        }
        this.doQuiver();
    }

    public void onDisable()
    {
        super.onDisable(); // hmm?
    }
    @Override
    public String getDisplayInfo()
    {
        if(hudMode.getName().equals("Arrows"))
        {
            return Integer.toString(getArrowCount());
        }
        else if(hudMode.getName().equals("Hits"))
        {
            return Integer.toString(arrowHits);
        }
        return null;
    }
}
