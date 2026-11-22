package com.fraynn.utilitary;

import net.fabricmc.api.ModInitializer;

public class UtilitaryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Utilitary.LOGGER.info("Initializing Utilitary: Fabric...");
        Utilitary.init();
    }
}
