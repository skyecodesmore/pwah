package dev.franckyi.pwah;

import dev.franckyi.pwah.block.PwahBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Objects;

public class Pwah implements ModInitializer {
    public static final Block PWAH_BLOCK = new PwahBlock(FabricBlockSettings.copy(Blocks.CARVED_PUMPKIN));

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier("pwah", "pwah"), PWAH_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("pwah", "pwah"), new BlockItem(PWAH_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isIntegratedServerRunning() || client.isPaused())
                return;
            assert client.player != null;
            this.runFor(Objects.requireNonNull(client.getServer()).getPlayerManager().getPlayer(client.player.getUuid()));
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!server.isDedicated())
                return;
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                this.runFor(player);
            }
        });
    }

    private void runFor(PlayerEntity player) {
        if (player == null || player.isSpectator())
            return;
        if (!player.isSneaking()) {
            player.removeScoreboardTag("isSneaking");
            return;
        }
        if (!player.getScoreboardTags().contains("isSneaking")) {
            player.addScoreboardTag("isSneaking");
            World world = player.world;
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
