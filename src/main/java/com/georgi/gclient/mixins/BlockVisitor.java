package com.georgi.gclient.mixins;


import com.georgi.gclient.GClient;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockVisitor {

    //Only renders ores
    @Inject(method = "shouldSideBeRendered(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)Z",
            at = @At("HEAD"), cancellable = true)
    private static void shouldSideBeRendered(BlockState adjacentState, IBlockReader blockState, BlockPos blockAccess, Direction pos, CallbackInfoReturnable<Boolean> cir) {
        if (GClient.xray.isEnabled) {
            // Despite the name adjacentState is actually the state of the current block.
            cir.setReturnValue(adjacentState.getBlock() instanceof OreBlock);
        }
    }

    // Makes the ores fully lit
    @Inject(method = "getAmbientOcclusionLightValue(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)F",
            at = @At("HEAD"), cancellable = true)
    private void getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (GClient.xray.isEnabled) {
            cir.setReturnValue(1.0f);
        }
    }
}

