package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.*;
import me.earth.earthhack.impl.modules.combat.quiver.modes.*;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.*;

public class Quiver extends Module {
    //TODO: FromInv - to get a bow from player inventory, place in hotbar and then quiver
    protected final Setting<HUDMode> hudMode     =
            register(new EnumSetting<>("HUDMode", HUDMode.Arrows));
    protected final Setting<SwitchMode> switchMode     =
            register(new EnumSetting<>("Switch", SwitchMode.Normal));
    protected final Setting<QuiverMode> quiverMode     =
            register(new EnumSetting<>("Mode", QuiverMode.Automatic));
    protected final Setting<Integer> delay   =
            register(new NumberSetting<>("Delay", 5, 0, 20));
    protected final Setting<Boolean> tpsSync =
            register(new BooleanSetting("TPS-Sync", false));
    protected final Setting<Boolean> blockedCheck =
            register(new BooleanSetting("BlockedCheck", false));
    protected final Setting<Boolean> mineBlocked =
            register(new BooleanSetting("MineBlocked", false));
    protected final Setting<RotationMode> rotateMode     =
            register(new EnumSetting<>("Rotation", RotationMode.Normal));
    protected final Setting<Boolean> spam =
            register(new BooleanSetting("Spam", true));

    public Quiver() {
        super("Quiver", Category.Combat);
        this.setData(new QuiverData(this));
        this.listeners.add(new ListenerMotion(this));
    }

    // stuff
    boolean hasBow;
    boolean blocked;
    int arrowHits = 0;
    int arrowCount;
    float oldPitch = mc.player.cameraPitch;
    float currentYaw = mc.player.cameraYaw;
    public void doQuiver()
    {
        if(mc.player == null){
            this.disable();
        }
        else
        {
            switch(rotateMode.getName()){
                case "Normal":
                    if(quiverMode.getName().equals("Automatic")){
                        mc.player.rotationPitch = 90.0f;
                        if(switchMode.getName().equals("Normal"))
                        {

                        }else
                        {

                        }

                    }else{
                        mc.player.rotationPitch = 90.0f;
                    }
                    break;
                case "Packet":
                    if(quiverMode.getName().equals("Automatic"))
                    {
                        if(!hasBow())
                        {
                            this.disable();
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(currentYaw, oldPitch, true));
                        }
                        else if(hasBow && arrowCount > 0)
                        {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(currentYaw, 90.0f, true));
                            if(switchMode.getName().equals("Normal") && hasBow()){
                                InventoryUtil.switchTo(InventoryUtil.findHotbarItem(Items.BOW));
                            }else if(switchMode.getName().equals("Silent") && hasBow()){
                                InventoryUtil.bypassSwitch(InventoryUtil.findHotbarItem(Items.BOW));
                            }else{
                                this.disable();
                            }
                        }
                    }
                    else
                    {
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(currentYaw, 90.0f, true));
                        if(hasBow() && arrowCount > 0){

                        }else{
                            this.disable();
                        }
                    }
                break;
            }
        }
    }

    public boolean hasBow(){
        if(InventoryUtil.getCount(Items.BOW) != 0)
            hasBow = true;
        else
            hasBow = false;
        return hasBow;
    }
    public int getArrowCount(){
        arrowCount = InventoryUtil.getCount(Items.TIPPED_ARROW);
        return arrowCount;
    }

    public void onEnable()
    {
        this.doQuiver();
    }

    public void onDisable()
    {
        this.disable();
        // maybe something else here? :P
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
