package dev.proststuff.utilitary;

import dev.proststuff.utilitary.api.config.ConfigManager;
import dev.proststuff.utilitary.api.config.serialization.metadata.SimpleMetadataType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilitary implements ModInitializer {
	public static final String ID = "utilitary";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static UtilitaryConfig CONFIG;

	@Override
	public void onInitialize() {
        ConfigManager.Result<UtilitaryConfig, SimpleMetadataType.Metadata> result = ConfigManager.load(UtilitaryConfig.ID, UtilitaryConfig.TYPE, UtilitaryConfig.FORMAT_SETTINGS);
		CONFIG = result.config();
		ConfigManager.save(UtilitaryConfig.ID, UtilitaryConfig.TYPE, UtilitaryConfig.FORMAT_SETTINGS, CONFIG);
	}

	public static Identifier of(String path) {
		return Identifier.fromNamespaceAndPath(ID, path);
	}

	public static boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}