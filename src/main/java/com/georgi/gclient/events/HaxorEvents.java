package com.georgi.gclient.events;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.MinecraftForge;

public class HaxorEvents {
    public static boolean dispatchShouldSideBeRendered(IBlockReader blockReader, BlockPos blockAccess, EnumFacing pos ){
        System.out.println("1");
        BlockPos blockpos = blockAccess.offset(pos);
        IBlockState iblockstate = blockReader.getBlockState(blockpos);
        return (iblockstate.getBlock() instanceof BlockOre);
        //return MinecraftForge.EVENT_BUS.post(new ShouldSideBeRenderedEvent(blockReader.getBlockState()));
    }
}
