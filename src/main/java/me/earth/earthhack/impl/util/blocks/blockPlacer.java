package me.earth.earthhack.impl.util.blocks;

import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static me.earth.earthhack.api.util.interfaces.Globals.mc;

public class blockPlacer {
    public boolean basicPlaceBlock (BlockPos blockpos, EnumHand hand, Boolean swing) {
        World world = Minecraft.getMinecraft().world;
        int x = blockpos.getX();
        int y = blockpos.getY();
        int z = blockpos.getZ();

        if (!world.isAirBlock(blockpos.east())) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockpos.east(), EnumFacing.EAST.getOpposite(), hand, x, y, z));
            return true;
        } else if (!world.isAirBlock(blockpos.north())) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockpos.north(), EnumFacing.NORTH.getOpposite(), hand, x, y, z));
            return true;
        } else if (!world.isAirBlock(blockpos.west())) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockpos.west(), EnumFacing.WEST.getOpposite(), hand, x, y, z));
            return true;
        } else if (!world.isAirBlock(blockpos.south())) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockpos.south(), EnumFacing.SOUTH.getOpposite(), hand, x, y, z));
            return true;
        } else if (!world.isAirBlock(blockpos.up())) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockpos.up(), EnumFacing.UP.getOpposite(), hand, x, y, z));
            return true;
        } else if (!world.isAirBlock(blockpos.down())) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockpos.down(), EnumFacing.DOWN.getOpposite(), hand, x, y, z));
            return true;
        } else {
            ChatUtil.sendMessage(TextColor.BLUE + "Cannot place block, no support! Check logs for more info.");
            System.out.println("Couldnt place block because all the place support checks failed.");
            return false;
        }

    }
}
