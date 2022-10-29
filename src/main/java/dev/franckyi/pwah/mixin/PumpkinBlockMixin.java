package dev.franckyi.pwah.mixin;

import dev.franckyi.pwah.Pwah;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PumpkinBlock.class)
public class PumpkinBlockMixin {
    @Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    public boolean redirectOnUse(World instance, BlockPos pos, BlockState state, int flags, BlockState stateCatpured, World world, BlockPos posCaptured, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Direction direction = hit.getSide();
        Direction direction2 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
        Block block = player.isSneaking() ? Pwah.PWAH_BLOCK : Blocks.CARVED_PUMPKIN;
        return instance.setBlockState(pos, block.getDefaultState().with(CarvedPumpkinBlock.FACING, direction2), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
    }
}
