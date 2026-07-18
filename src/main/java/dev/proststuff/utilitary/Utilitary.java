package dev.proststuff.utilitary;

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
		CONFIG = UtilitaryConfig.load();
		UtilitaryConfig.save();
	}

	public static Identifier of(String path) {
		return Identifier.fromNamespaceAndPath(ID, path);
	}

	public static boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}