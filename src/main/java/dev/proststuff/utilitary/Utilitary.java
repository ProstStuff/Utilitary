package dev.proststuff.utilitary;

import dev.proststuff.utilitary.config.Config;
import dev.proststuff.utilitary.config.network.ClientConfigSync;
import dev.proststuff.utilitary.config.network.ServerBoundConfigSyncPacket;
import dev.proststuff.utilitary.config.utility.ConfigEnvironment;
import dev.proststuff.utilitary.config.utility.UtilitaryFileWatcher;
import dev.proststuff.utilitary.persistent.PersistentData;
import dev.proststuff.utilitary.persistent.PersistentDataClient;
import dev.proststuff.utilitary.persistent.PersistentDataSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public class Utilitary implements ModInitializer, ClientModInitializer {
	public static final String ID = "utilitary";
	public static final String NAME = "Utilitary";
	public static final UtilitaryConfig CONFIG = new UtilitaryConfig();
	public static final Logger LOGGER = CONFIG.getLogger();

	private static MinecraftServer server;

	@Override
	public void onInitialize() {
		Config.loadSided(ConfigEnvironment.STARTUP, true);

		PersistentData.register();

		PayloadTypeRegistry.playS2C().register(ServerBoundConfigSyncPacket.ID, ServerBoundConfigSyncPacket.PACKET_CODEC);

		PayloadTypeRegistry.playS2C().register(PersistentDataSyncPacket.ID, PersistentDataSyncPacket.PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(PersistentDataSyncPacket.ID, PersistentDataSyncPacket.PACKET_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(PersistentDataSyncPacket.ID, (payload, context) -> {
			ServerPlayerEntity player = context.player();
			context.server().execute(() -> PersistentData.sync(player));
		});

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			Utilitary.server = server;
			Config.loadSided(ConfigEnvironment.SERVER, true);
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			Utilitary.server = null;
			UtilitaryFileWatcher.stopWatching(ConfigEnvironment.SERVER);
		});

		ServerPlayerEvents.JOIN.register(serverPlayer -> {
			Config.loadSided(ConfigEnvironment.SERVER, true);
            PersistentData.sync(serverPlayer);
		});

		Config.loadSided(ConfigEnvironment.COMMON, true);
	}

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(ServerBoundConfigSyncPacket.ID, ClientConfigSync::receiveChunk);

		ClientPlayNetworking.registerGlobalReceiver(PersistentDataSyncPacket.ID, (packet, context) -> {context.client().execute(() -> PersistentDataClient.update(packet));});

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			Config.loadSided(ConfigEnvironment.CLIENT, true);
		});
	}

	public static Identifier of(String path) {
		return Identifier.of(ID, path);
	}

	public static MinecraftServer getServer() {
		return server;
	}
}