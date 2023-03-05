package me.earth.earthhack.impl.modules.player.automine.util;

import me.earth.earthhack.impl.util.minecraft.blocks.SpecialBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.HashSet;
import java.util.Set;


public class ShulkerConstellation implements IConstellation
{
    private final BlockPos pos;
    protected Set<Block> getBlocks()
    {
        Set<Block> result = new HashSet<>();
        {
            result.addAll(SpecialBlocks.SHULKERS);
        }

        return result;
    }

    public ShulkerConstellation(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public boolean isAffected(BlockPos pos, IBlockState state)
    {
        return this.pos.equals(pos) && state.getBlock() != getBlocks();
    }

    @Override
    public boolean isValid(IBlockAccess world, boolean checkPlayerState)
    {
        return world.getBlockState(pos).getBlock() == getBlocks();
    }

    @Override
    public boolean cantBeImproved()
    {
        return false;
    }

}