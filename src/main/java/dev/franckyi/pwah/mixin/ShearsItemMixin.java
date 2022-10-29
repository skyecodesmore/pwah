package dev.franckyi.pwah.mixin;

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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @Inject(method = "useOnBlock", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", value = "INVOKE"))
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, World world, BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() instanceof PumpkinBlock && context.getPlayer() != null && context.getPlayer().isSneaking()) {
            blockState.getBlock().onUse(blockState, world, blockPos, context.getPlayer(), context.getHand(), new BlockHitResult(context.getHitPos(), context.getSide(), blockPos, false));
        }
    }
}
