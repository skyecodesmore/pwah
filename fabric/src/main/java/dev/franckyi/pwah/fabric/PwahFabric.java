package dev.franckyi.pwah.fabric;

import dev.franckyi.pwah.Pwah;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class PwahFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, Pwah.PWAH_BLOCK_ID, Pwah.PWAH_BLOCK);
        Registry.register(Registries.ITEM, Pwah.PWAH_BLOCK_ID, Pwah.PWAH_ITEM);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> content.add(Pwah.PWAH_ITEM));
        ClientTickEvents.END_CLIENT_TICK.register(Pwah::onClientTick);
        ServerTickEvents.END_SERVER_TICK.register(Pwah::onServerTick);
    }
}