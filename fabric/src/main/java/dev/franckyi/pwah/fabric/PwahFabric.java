package dev.franckyi.pwah.fabric;

import dev.franckyi.pwah.Pwah;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.registry.Registry;

public class PwahFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, Pwah.PWAH_BLOCK_ID, Pwah.PWAH_BLOCK);
        Registry.register(Registry.ITEM, Pwah.PWAH_BLOCK_ID, Pwah.PWAH_ITEM);
        ClientTickEvents.END_CLIENT_TICK.register(Pwah::onClientTick);
        ServerTickEvents.END_SERVER_TICK.register(Pwah::onServerTick);
    }
}