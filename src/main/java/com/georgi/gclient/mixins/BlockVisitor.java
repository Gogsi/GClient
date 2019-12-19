package com.georgi.gclient.mixins;


import com.georgi.gclient.GClient;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockVisitor {

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private static void shouldSideBeRendered(IBlockState adjacentState, IBlockReader blockState, BlockPos blockAccess, EnumFacing pos, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("Ayy");
        if (GClient.xray.isEnabled) {
            BlockPos blockpos = blockAccess.offset(pos);
            IBlockState iblockstate = blockState.getBlockState(blockpos);
            cir.setReturnValue(iblockstate.getBlock() instanceof BlockOre);
        }
    }
}

