package dev.franckyi.pwah.forge;

import dev.franckyi.pwah.Pwah;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Pwah.MOD_ID)
public class PwahForge {
    public PwahForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBlock);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItem);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::buildContents);
    }

    private void onInit(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
    }

    private void registerBlock(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(Pwah.PWAH_BLOCK_ID, Pwah.PWAH_BLOCK));
    }

    private void registerItem(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(Pwah.PWAH_BLOCK_ID, Pwah.PWAH_ITEM));
    }

    private void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ItemGroups.BUILDING_BLOCKS) {
            event.accept(() -> Pwah.PWAH_ITEM);
        }
    }

    private void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) Pwah.onClientTick(MinecraftClient.getInstance());
    }

    private void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) Pwah.onServerTick(event.getServer());
    }



}