package me.earth.earthhack.impl.modules.player.pearlphase;

import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.impl.util.client.ModuleUtil;
import me.earth.earthhack.impl.util.client.SimpleData;
import me.earth.earthhack.impl.util.helpers.disabling.DisablingModule;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import me.earth.earthhack.impl.util.thread.Locks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;



// TODO: thingy that makes crystals fall on us
public class PearlPhase extends DisablingModule
{


    protected final Setting<Boolean> bypass =
            register(new BooleanSetting("Bypass", true));



    public PearlPhase()
    {
        super("PearlPhase", Category.Player);
        SimpleData data = new SimpleData(
                this, "Enable this to throw a pearl to phase.");
        this.setData(data);
    }

    @Override
    protected void onEnable()
    {
        super.onEnable();
        if (mc.player == null || mc.world == null)
        {
            this.toggle();
            return;
        }

        int slot = InventoryUtil.findHotbarItem(Items.ENDER_PEARL);
        int slot2 = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);

        if (slot != -1)  {
            if (slot2 == -1 && bypass.getValue()) {
                ModuleUtil.disable(this, TextColor.RED + "Disabled, no Obsidian.");
            } else {
                Locks.acquire(Locks.PLACE_SWITCH_LOCK, () ->
                {
                    int lastSlot = mc.player.inventory.currentItem;

                    float aimYaw = mc.player.rotationYaw;
                    float aimPitch = 80;



                    if (bypass.getValue()) {
                        InventoryUtil.switchTo(slot2);


                        int x = (int) Math.floor(mc.player.posX);
                        int z = (int) Math.floor(mc.player.posZ);
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(x, 0, z),
                                EnumFacing.DOWN, EnumHand.MAIN_HAND,
                                x, -1, z));


                        InventoryUtil.switchTo(lastSlot);
                    }

                    InventoryUtil.switchTo(slot);
                    // rotate
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(aimYaw, aimPitch, mc.player.onGround));

                    mc.playerController.processRightClick(
                            mc.player, mc.world, InventoryUtil.getHand(slot));

                    InventoryUtil.switchTo(lastSlot);
                    this.toggle();
                });
            }

        } else {

            if (slot == -1) {
                if (slot2 == -1 && bypass.getValue()) {
                    ModuleUtil.disable(this, TextColor.RED + "Disabled, no Obsidian and Ender Pearl.");
                } else {
                    ModuleUtil.disable(this, TextColor.RED + "Disabled, no Ender Pearl.");
                }

            }

        }


    }
}
