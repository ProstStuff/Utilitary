package dev.proststuff.utilitary.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.utility.config.ConfigPress;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientConfigSync {
    private static final Map<String, List<String>> pendingChunks = new HashMap<>();
    private static final Map<String, Long> chunkTimestamps = new HashMap<>();
    private static final long TIMEOUT_MS = 30_000;

    static {
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "RW Config Sync Thread");
            t.setDaemon(true);
            return t;
        }).scheduleAtFixedRate(ClientConfigSync::cleanupExpired, 10, 10, TimeUnit.SECONDS);
    }

    public static void receiveChunk(ServerBoundConfigSyncPacket packet) {
        String managerName = packet.managerName();
        ConfigManager configManager = ConfigManager.getManager(managerName);

        if (configManager == null) {
            Utilitary.UTILITARY_CONFIG.error("This client does not have a ConfigManager with name {}", managerName);
            return;
        }

        chunkTimestamps.put(managerName, System.currentTimeMillis());
        pendingChunks.computeIfAbsent(managerName, k -> new ArrayList<>()).add(packet.data());

        if (packet.index() + 1 == packet.total()) {
            try {
                byte[] allData = Base64.getDecoder().decode(String.join("", pendingChunks.remove(managerName)));
                chunkTimestamps.remove(managerName);

                byte[] decompressed = packet.isCompressed()
                        ? ConfigPress.decompress(allData)
                        : allData;

                String jsonStr = new String(decompressed, StandardCharsets.UTF_8);
                JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
                ConfigManager manager = ConfigManager.getManagerOrThrow(managerName);

                manager.info("Received and decompressed '{}' config ({} chunks)",
                        managerName, packet.total());

                if (manager.getConfigFile(packet.configFileName()) != null) {
                    manager.getConfigFile(packet.configFileName()).decode(json);
                }
            } catch (Exception e) {
                configManager.errorWithStackTrace(e, "Unable to decompress received config data");
            }
        }
    }

    private static void cleanupExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> it = chunkTimestamps.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            if (now - entry.getValue() > TIMEOUT_MS) {
                String managerName = entry.getKey();
                it.remove();
                pendingChunks.remove(managerName);

                ConfigManager configManager = ConfigManager.getManager(managerName);
                if (configManager == null) {
                    Utilitary.UTILITARY_CONFIG.error("This client does not have a ConfigManager with name {}, but their config data is discarded due to timeout", managerName);
                    return;
                }
                configManager.warn("Timeout: Discarded incomplete sync");
            }
        }
    }
}
