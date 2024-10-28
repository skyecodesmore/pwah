package com.skyecodes.pwah.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Arrays;

public class PwahBlock extends HorizontalFacingBlock implements Fertilizable {
    public static final MapCodec<PwahBlock> CODEC = createCodec(PwahBlock::new);
    public static final IntProperty BREEDING = IntProperty.of("breeding", 0, 7);
    public static final BooleanProperty FILLED = BooleanProperty.of("filled");

    public PwahBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(BREEDING, 0)
                .with(FILLED, false));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, BREEDING, FILLED);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(Items.BUCKET) && state.get(FILLED) && hit.getSide().equals(state.get(FACING))) {
            ItemStack milk = Items.MILK_BUCKET.getDefaultStack();
            milk.set(DataComponentTypes.LORE, new LoreComponent(Arrays.asList(Text.empty(), Text.literal("A very special kind of milk...").formatted(Formatting.GRAY))));
            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
            ItemStack newStack = ItemUsage.exchangeStack(stack, player, milk);
            player.setStackInHand(hand, newStack);
            world.setBlockState(pos, state.cycle(FILLED));
            return ActionResult.SUCCESS;
        } else {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return !state.get(FILLED);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        state = state.cycle(BREEDING);
        if (state.get(BREEDING) == 0) {
            state = state.cycle(FILLED);
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 0.8F + random.nextFloat() * 0.4F);
        }
        world.setBlockState(pos, state);
    }

    @Override
    public BlockPos getFertilizeParticlePos(BlockPos pos) {
        return pos.up(1);
    }
}
