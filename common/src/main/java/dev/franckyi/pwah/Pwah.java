package dev.franckyi.pwah;

import dev.franckyi.pwah.block.PwahBlock;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Objects;

public class Pwah {
    public static final String MOD_ID = "pwah";
    public static final Identifier PWAH_BLOCK_ID = new Identifier(MOD_ID, "pwah");
    public static final Block PWAH_BLOCK = new PwahBlock(AbstractBlock.Settings.copy(Blocks.CARVED_PUMPKIN));
    public static final Item PWAH_ITEM = new BlockItem(PWAH_BLOCK, new Item.Settings());

    public static void onClientTick(MinecraftClient client) {
        if (!client.isIntegratedServerRunning() || client.isPaused())
            return;
        assert client.player != null;
        tryBreed(Objects.requireNonNull(client.getServer()).getPlayerManager().getPlayer(client.player.getUuid()));
    }

    public static void onServerTick(MinecraftServer server) {
        if (!server.isDedicated())
            return;
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            Pwah.tryBreed(player);
        }
    }

    private static void tryBreed(PlayerEntity player) {
        if (player == null || player.isSpectator())
            return;
        if (!player.isSneaking()) {
            player.removeScoreboardTag("isSneaking");
            return;
        }
        if (!player.getCommandTags().contains("isSneaking")) {
            player.getCommandTags().add("isSneaking");
            World world = player.getWorld();
            BlockPos playerPos = player.getBlockPos();
            Direction playerFacing = player.getHorizontalFacing();
            BlockPos blockPos = playerPos.offset(playerFacing);
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock() == Pwah.PWAH_BLOCK && blockState.get(HorizontalFacingBlock.FACING) == playerFacing.getOpposite()) {
                if (BoneMealItem.useOnFertilizable(new ItemStack(Items.BONE_MEAL), world, blockPos)) {
                    if (!world.isClient()) world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                }
            }
        }
    }
}
