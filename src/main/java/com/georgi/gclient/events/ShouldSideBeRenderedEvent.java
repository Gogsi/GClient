package com.georgi.gclient.events;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event;

public class ShouldSideBeRenderedEvent extends Event{
    private final IBlockState state;
    private final BlockPos blockAccess;
    private final EnumFacing pos;

    public ShouldSideBeRenderedEvent(IBlockState state, BlockPos blockAccess, EnumFacing pos)
    {
        this.state = state;
        this.blockAccess = blockAccess;
        this.pos = pos;
    }

    public IBlockState getState()
    {
        return state;
    }
}
/*
 BlockPos blockpos = blockAccess.offset(pos);
 IBlockState iblockstate = blockState.getBlockState(blockpos);
 */