package dev.franckyi.pwah.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class PwahBlock extends HorizontalFacingBlock implements Fertilizable {
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, BREEDING, FILLED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.BUCKET) && state.get(FILLED) && hit.getSide().equals(state.get(FACING))) {
            ItemStack stack = Items.MILK_BUCKET.getDefaultStack();
            NbtList desc = new NbtList();
            desc.add(NbtString.of(""));
            desc.add(NbtString.of(Text.Serializer.toJson(Text.literal("A very special kind of milk...").formatted(Formatting.GRAY))));
            stack.getOrCreateSubNbt("display").put("Lore", desc);
            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
            ItemStack newStack = ItemUsage.exchangeStack(itemStack, player, stack);
            player.setStackInHand(hand, newStack);
            world.setBlockState(pos, state.cycle(FILLED));
            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
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
}
