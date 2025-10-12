package dev.proststuff.reconstruct_what.utility;

import dev.proststuff.reconstruct_what.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;

public class RWEvents {
    public static void playerJoined(ServerPlayer player) {
        ConfigManager.getManagers().forEach(m -> m.syncToPlayer(player));
    }
}