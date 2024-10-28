package com.skyecodes.pwah.fabric.mixin;

import com.skyecodes.pwah.Pwah;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Redirect(method = "useOnBlock", at = @At(target = "Lnet/minecraft/item/BoneMealItem;useOnFertilizable(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", value = "INVOKE"))
    public boolean stuff(ItemStack stack, World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() != Pwah.PWAH_BLOCK && BoneMealItem.useOnFertilizable(stack, world, pos);
    }
}
