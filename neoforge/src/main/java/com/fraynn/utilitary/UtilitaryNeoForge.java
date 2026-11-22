package com.fraynn.utilitary;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Utilitary.ID)
public class UtilitaryNeoForge {
    public UtilitaryNeoForge(IEventBus eventBus) {
        Utilitary.LOGGER.info("Initializing Utilitary: NeoForge...");
        Utilitary.init();
    }
}