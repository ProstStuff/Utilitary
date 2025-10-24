package dev.proststuff.utilitary;

import dev.proststuff.utilitary.config.ClientConfigSync;
import dev.proststuff.utilitary.config.ConfigManager;
import dev.proststuff.utilitary.config.ConfigOption;
import dev.proststuff.utilitary.config.ServerBoundConfigSyncPacket;
import dev.proststuff.utilitary.config.template.ConfigBool;
import dev.proststuff.utilitary.config.template.ConfigColor;
import dev.proststuff.utilitary.config.template.ConfigString;
import dev.proststuff.utilitary.utility.config.ConfigEnvironment;
import dev.proststuff.utilitary.utility.config.ConfigFileWatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class Utilitary implements ModInitializer, ClientModInitializer {
	public static final String ID = "utilitary";
	public static final String NAME = "Utilitary";
	public static final ConfigManager UTILITARY_CONFIG = new ConfigManager(NAME).setDebugEnable(FabricLoader.getInstance().isDevelopmentEnvironment());

	public static MinecraftServer SERVER;

	static {
		UTILITARY_CONFIG.newFile("utilitaryStartup", ConfigEnvironment.STARTUP)
				.add(new ConfigBool("debug", false).listen((v) -> UTILITARY_CONFIG.setDebugEnable(v.get())))
				.add(new ConfigString("introduction", "This is a startup config, all startup config is loaded at the start of Utilitary onInitialize(), common is loaded at the end of the library onInitialize()"))
				.add(new ConfigString("tips", "It is recommended to use a static initializer to register your config"));

		UTILITARY_CONFIG.newFile("utilities_common", ConfigEnvironment.COMMON)
				.add(new ConfigBool("enabled", true))
				.add(new ConfigString("introduction", "This is a config with COMMON environment."))
				.add(new ConfigOption("features")
						.add(new ConfigString("nesting", "Nesting:"))
						.add(new ConfigOption("nested1")
								.add(new ConfigString("two", "Another nest"))
								.add(new ConfigOption("nested2")
										.add(new ConfigOption("nested3"))
										.add(new ConfigBool("isInfinite", true))
										.add(new ConfigString("Limit", "There is no limit to this, I think..."))
								)
						)
						.add(new ConfigColor("color", 0x00ffff))
						.add(new ConfigString("configSync", "COMMON and SERVER config is synced to the player."))
				)
				.add(new ConfigBool("willThisSyncedToClients", false).runtimeOnly()); // This option is runtime only, not written/read in the config file but accessible during runtime.

		UTILITARY_CONFIG.newFile("client", ConfigEnvironment.CLIENT)
				.add(new ConfigString("about", "Client config"));

		UTILITARY_CONFIG.newFile("utilitaryServer", ConfigEnvironment.SERVER)
				.add(new ConfigString("about", "This is server config, similar to COMMON but is relative to the world."));
	}

	@Override
	public void onInitialize() {
		ConfigManager.loadFor(ConfigEnvironment.STARTUP);
		PayloadTypeRegistry.playS2C().register(ServerBoundConfigSyncPacket.ID, ServerBoundConfigSyncPacket.PACKET_CODEC);
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			SERVER = server;
			ConfigManager.loadFor(ConfigEnvironment.SERVER);
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			SERVER = null;
			ConfigFileWatcher.stopWatching(ConfigEnvironment.SERVER);
		});
		ServerPlayerEvents.JOIN.register(ConfigManager::syncToPlayer);

		ConfigManager.loadFor(ConfigEnvironment.COMMON);
	}

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(ServerBoundConfigSyncPacket.ID, (packet, context) -> {
			context.client().execute(() -> ClientConfigSync.receiveChunk(packet));
		});

		ConfigManager.loadFor(ConfigEnvironment.CLIENT);
	}
}