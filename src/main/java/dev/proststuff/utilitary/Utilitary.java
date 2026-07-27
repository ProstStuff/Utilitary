package dev.proststuff.utilitary;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
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

		ClientLifecycleEvents.CLIENT_STARTED.register((_) -> UtilitaryConfig.save());
	}

	public static Identifier of(String path) {
		return Identifier.fromNamespaceAndPath(ID, path);
	}
}