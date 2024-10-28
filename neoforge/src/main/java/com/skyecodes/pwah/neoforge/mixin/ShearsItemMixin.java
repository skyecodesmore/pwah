package com.skyecodes.pwah.neoforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @Inject(method = "useOnBlock", at = @At(target = "Lnet/minecraft/block/BlockState;getToolModifiedState(Lnet/minecraft/item/ItemUsageContext;Lnet/neoforged/neoforge/common/ItemAbility;Z)Lnet/minecraft/block/BlockState;", value = "INVOKE"))
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, @Local World world, @Local BlockPos blockPos, @Local BlockState blockState) {
        System.out.println("MIXIN");
        if (blockState.getBlock() instanceof PumpkinBlock && context.getPlayer() != null && context.getPlayer().isSneaking()) {
            blockState.onUseWithItem(context.getStack(), world, context.getPlayer(), context.getHand(), new BlockHitResult(context.getHitPos(), context.getSide(), blockPos, false));
        }
    }
}
