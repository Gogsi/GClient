package com.georgi.gclient.events;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event;

public class ShouldSideBeRenderedEvent extends Event{
    private final BlockState state;
    private final BlockPos blockAccess;
    private final Direction pos;

    public ShouldSideBeRenderedEvent(BlockState state, BlockPos blockAccess, Direction pos)
    {
        this.state = state;
        this.blockAccess = blockAccess;
        this.pos = pos;
    }

    public BlockState getState()
    {
        return state;
    }
}
/*
 BlockPos blockpos = blockAccess.offset(pos);
 IBlockState iblockstate = blockState.getBlockState(blockpos);
 */