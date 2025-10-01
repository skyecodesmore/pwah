package com.skyecodes.pwah.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.skyecodes.pwah.Pwah;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PumpkinBlock.class)
public class PumpkinBlockMixin {
    @Redirect(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    public boolean redirectOnUse(World instance, BlockPos pos, BlockState state, int flags, @Local(argsOnly = true) PlayerEntity player, @Local(ordinal = 1) Direction direction) {
        Block block = player.isSneaking() ? Pwah.PWAH_BLOCK : Blocks.CARVED_PUMPKIN;
        return instance.setBlockState(pos, block.getDefaultState().with(CarvedPumpkinBlock.FACING, direction), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
    }
}
