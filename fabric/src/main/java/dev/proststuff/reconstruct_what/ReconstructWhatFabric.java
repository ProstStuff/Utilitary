package dev.proststuff.reconstruct_what;

import dev.proststuff.reconstruct_what.client.ClientConfigHelper;
import dev.proststuff.reconstruct_what.config.ClientBoundConfigSyncPacket;
import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.utility.RWEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;

public class ReconstructWhatFabric implements ModInitializer, ClientModInitializer {
    
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(ClientBoundConfigSyncPacket.TYPE, ClientBoundConfigSyncPacket.STREAM_CODEC);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> ReconstructWhat.loadConfigs(ConfigHelper.ConfigType.SERVER, server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ConfigHolder.stopWatching(ConfigHelper.ConfigType.SERVER));
        ServerPlayerEvents.JOIN.register(RWEvents::playerJoined);
        ClientPlayNetworking.registerGlobalReceiver(ClientBoundConfigSyncPacket.TYPE, (packet, context) -> {
            Minecraft.getInstance().execute(() -> ClientConfigHelper.receiveChunk(packet));
        });
        ReconstructWhat.init();
        ReconstructWhat.loadConfigs(ConfigHelper.ConfigType.COMMON, null);
    }

    @Override
    public void onInitializeClient() {


        ReconstructWhat.loadConfigs(ConfigHelper.ConfigType.CLIENT, null);
    }
}