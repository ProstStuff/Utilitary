package dev.proststuff.reconstruct_what.utility;

import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;
import net.minecraft.server.level.ServerPlayer;

public class RWEvents {
    private static final IPlatformHelper PLATFORM = ReconstructWhat.getPlatform();

    public static void playerJoined(ServerPlayer player) {
        for (ConfigManager manager : ConfigManager.getManagers()) {
            manager.syncToPlayer(player);
        }
    }
}
