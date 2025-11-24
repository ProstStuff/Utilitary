package dev.proststuff.utilitary;

import dev.proststuff.utilitary.config.ClientConfigSync;
import dev.proststuff.utilitary.config.ConfigManager;
import dev.proststuff.utilitary.config.ConfigOption;
import dev.proststuff.utilitary.config.ServerBoundConfigSyncPacket;
import dev.proststuff.utilitary.config.template.ConfigBool;
import dev.proststuff.utilitary.config.template.ConfigColor;
import dev.proststuff.utilitary.config.template.ConfigString;
import dev.proststuff.utilitary.config.utility.ConfigEnvironment;
import dev.proststuff.utilitary.config.utility.ConfigFileWatcher;
import dev.proststuff.utilitary.persistent.PersistentData;
import dev.proststuff.utilitary.persistent.PersistentDataClient;
import dev.proststuff.utilitary.persistent.PersistentDataSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public class Utilitary implements ModInitializer, ClientModInitializer {
	public static final String ID = "utilitary";
	public static final String NAME = "Utilitary";
	public static final ConfigManager CONFIG = new ConfigManager(NAME, "Utilitary").setDebugEnable(FabricLoader.getInstance().isDevelopmentEnvironment());
	public static final Logger LOGGER = CONFIG.LOGGER;

	private static MinecraftServer server;

	private static final ConfigBool debug = (ConfigBool) new ConfigBool("debug", false).changed(v -> {
		CONFIG.setDebugEnable(v.get());
		CONFIG.info("Debug enabled");
	});

	static {
		CONFIG.newFile("utilitaryStartup", ConfigEnvironment.STARTUP)
				.add(debug)
				.add(new ConfigString("about", "This is a startup config, all startup config is loaded at the start of Utilitary onInitialize(), common is loaded at the end of the library onInitialize()"))
				.add(new ConfigString("tips", "It is recommended to use a static initializer to register your config"))
				.add(new ConfigString("note", "Config is loaded asynchronously, use `.changed()` to detect changes or when the value is loaded"));

		CONFIG.newFile("utilities_common", ConfigEnvironment.COMMON)
				.add(new ConfigBool("enabled", true))
				.add(new ConfigString("about", "This is a config with COMMON environment. Players config will be synced to the server's config."))
				.add(new ConfigOption("features (partial)")
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
						.add(new ConfigString("configSync", "COMMON and server config is synced to the player."))
				)
				.add(new ConfigBool("willThisSyncedToClients", false).runtimeOnly()); // This option is runtime only, not written/read in the config file but accessible during runtime.

		CONFIG.newFile("client", ConfigEnvironment.CLIENT)
				.add(new ConfigString("about", "Client config, does not replicate to every players"));

		CONFIG.newFile("utilitaryServer", ConfigEnvironment.SERVER)
				.add(new ConfigString("about", "This is server config, similar to COMMON but is relative to the world."))
				.add(new ConfigString("note", "Sooner or later, the server config will be in `config` instead, and as a default config, `serverconfig` is the copy of it, the config will use `serverconfig`."));
	}

	@Override
	public void onInitialize() {
		ConfigManager.loadFor(ConfigEnvironment.STARTUP);
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
			ConfigManager.loadFor(ConfigEnvironment.SERVER);
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			Utilitary.server = null;
			ConfigFileWatcher.stopWatching(ConfigEnvironment.SERVER);
		});

		ServerPlayerEvents.JOIN.register(serverPlayer -> {
			ConfigManager.syncToPlayer(serverPlayer);
            PersistentData.sync(serverPlayer);
		});

		ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> PersistentData.sync(newPlayer)));

		ConfigManager.loadFor(ConfigEnvironment.COMMON);
	}

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(ServerBoundConfigSyncPacket.ID, (packet, context) -> context.client().execute(() -> ClientConfigSync.receiveChunk(packet)));
		ClientPlayNetworking.registerGlobalReceiver(PersistentDataSyncPacket.ID, (packet, context) -> {context.client().execute(() -> PersistentDataClient.update(packet));});

		ConfigManager.loadFor(ConfigEnvironment.CLIENT);
	}

	public static Identifier of(String path) {
		return Identifier.of(ID, path);
	}

	public static MinecraftServer getServer() {
		return server;
	}
}