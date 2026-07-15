package dev.proststuff.utilitary;

import dev.proststuff.utilitary.client.UtilitaryClientEvents;
import net.fabricmc.api.ClientModInitializer;

public class UtilitaryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UtilitaryClientEvents.init();
    }
}
