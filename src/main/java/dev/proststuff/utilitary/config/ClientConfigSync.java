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
        String mangerName = packet.managerName();

        chunkTimestamps.put(mangerName, System.currentTimeMillis());
        pendingChunks.computeIfAbsent(mangerName, k -> new ArrayList<>()).add(packet.data());

        if (packet.index() + 1 == packet.total()) {
            try {
                byte[] allData = Base64.getDecoder().decode(String.join("", pendingChunks.remove(mangerName)));
                chunkTimestamps.remove(mangerName);

                byte[] decompressed = packet.isCompressed()
                        ? ConfigPress.decompress(allData)
                        : allData;

                String jsonStr = new String(decompressed, StandardCharsets.UTF_8);
                JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
                ConfigManager manager = ConfigManager.getManagerOrThrow(mangerName);

                manager.info("Received and decompressed '{}' config ({} chunks)",
                        mangerName, packet.total());

                if (manager.getConfigFile(packet.configFileName()) != null) {
                    manager.getConfigFile(packet.configFileName()).decode(json);
                }
            } catch (Exception e) {
                Utilitary.UTILITARY_CONFIG.error("Failed to decode config for '{}': {}", mangerName, e);
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

                ConfigManager manager = ConfigManager.getManagerOrThrow(managerName);
                manager.warn("Timeout: Discarded incomplete sync");
            }
        }
    }
}
