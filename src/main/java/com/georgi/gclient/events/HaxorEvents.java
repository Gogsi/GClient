package com.georgi.gclient.events;

import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.MinecraftForge;

public class HaxorEvents {
    public static boolean dispatchShouldSideBeRendered(IBlockReader blockReader, BlockPos blockAccess, Direction pos ){
        System.out.println("1");
        BlockPos blockpos = blockAccess.offset(pos);
        BlockState iblockstate = blockReader.getBlockState(blockpos);
        return (iblockstate.getBlock() instanceof OreBlock);
        //return MinecraftForge.EVENT_BUS.post(new ShouldSideBeRenderedEvent(blockReader.getBlockState()));
    }
}
