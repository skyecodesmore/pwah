package com.skyecodes.pwah.neoforge;

import com.skyecodes.pwah.Pwah;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Pwah.MOD_ID)
public class PwahNeoForge {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            Registries.BLOCK,
            Pwah.MOD_ID
    );
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            Registries.ITEM,
            Pwah.MOD_ID
    );
    public static final DeferredHolder<Block, Block> PWAH_BLOCK = BLOCKS.register(
            "pwah",
            () -> Pwah.PWAH_BLOCK
    );
    public static final DeferredHolder<Item, Item> PWAH_ITEM = ITEMS.register(
            "pwah",
            () -> Pwah.PWAH_ITEM
    );

    public PwahNeoForge(IEventBus modBus) {
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        modBus.addListener(this::onInit);
        modBus.addListener(this::buildContents);
    }

    private void onInit(FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        NeoForge.EVENT_BUS.addListener(this::onServerTick);
    }

    private void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ItemGroups.BUILDING_BLOCKS) {
            event.add(Pwah.PWAH_ITEM);
        }
    }

    private void onClientTick(ClientTickEvent.Post event) {
        Pwah.onClientTick(MinecraftClient.getInstance());
    }

    private void onServerTick(ServerTickEvent.Post event) {
        Pwah.onServerTick(event.getServer());
    }
}