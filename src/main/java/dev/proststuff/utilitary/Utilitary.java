package dev.proststuff.utilitary;

import dev.proststuff.utilitary.api.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilitary implements ModInitializer {
	public static final String ID = "utilitary";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static UtilitaryConfig CONFIG;

	@Override
	public void onInitialize() {
		CONFIG = ConfigManager.load(UtilitaryConfig.ID, UtilitaryConfig.TYPE);
		ConfigManager.save(UtilitaryConfig.ID, UtilitaryConfig.TYPE, CONFIG);
	}

	public static boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}