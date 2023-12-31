package me.earth.earthhack.impl.modules.player.pearlphase;

import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.util.client.ModuleUtil;
import me.earth.earthhack.impl.util.client.SimpleData;
import me.earth.earthhack.impl.util.helpers.disabling.DisablingModule;
import me.earth.earthhack.impl.util.minecraft.CooldownBypass;
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

    protected final Setting<CooldownBypass> cooldownBypass =
            register(new EnumSetting<>("CoolDownBypass", CooldownBypass.None));
    protected final Setting<Boolean> obby =
            register(new BooleanSetting("Obsidian", false));
    protected final Setting<Integer> pitch =
            register(new NumberSetting<>("Pitch", 80, -90, 90));


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
            toggle();
            return;
        }

        int pearlSlot = InventoryUtil.findHotbarItem(Items.ENDER_PEARL);
        int obbySlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);

        if (pearlSlot != -1)  {
            if (obbySlot == -1 && obby.getValue()) {
                ModuleUtil.disable(this, TextColor.RED + "Disabled, no Obsidian.");
            } else {
                Locks.acquire(Locks.PLACE_SWITCH_LOCK, () ->
                {
                    int lastSlot = mc.player.inventory.currentItem;

                    float aimYaw = mc.player.rotationYaw;
                    float aimPitch = pitch.getValue();



                    if (obby.getValue()) {
                        cooldownBypass.getValue().switchTo(obbySlot);


                        int x = (int) Math.floor(mc.player.posX);
                        int z = (int) Math.floor(mc.player.posZ);
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(x, 0, z),
                                EnumFacing.DOWN, EnumHand.MAIN_HAND,
                                x, -1, z));


                        cooldownBypass.getValue().switchTo(lastSlot);
                    }

                    cooldownBypass.getValue().switchTo(pearlSlot);
                    // rotate
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(aimYaw, aimPitch, mc.player.onGround));

                    mc.playerController.processRightClick(mc.player, mc.world, InventoryUtil.getHand(pearlSlot));

                    cooldownBypass.getValue().switchTo(lastSlot);
                    toggle();
                });
            }

        } else {

            if (pearlSlot == -1) {
                if (obbySlot == -1 && obby.getValue()) {
                    ModuleUtil.disable(this, TextColor.RED + "Disabled, no Obsidian and Ender Pearl.");
                } else {
                    ModuleUtil.disable(this, TextColor.RED + "Disabled, no Ender Pearl.");
                }

            }

        }


    }
}
