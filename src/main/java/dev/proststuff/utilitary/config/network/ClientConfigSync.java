package dev.proststuff.utilitary.config.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.config.Config;
import dev.proststuff.utilitary.config.ConfigFile;
import dev.proststuff.utilitary.config.utility.ConfigCompressor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

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
            Thread t = new Thread(r, "Config Sync Thread");
            t.setDaemon(true);
            return t;
        }).scheduleAtFixedRate(ClientConfigSync::cleanupExpired, 10, 10, TimeUnit.SECONDS);
    }

    public static void receiveChunk(ServerBoundConfigSyncPacket packet, ClientPlayNetworking.Context context) {
        String name = packet.configName();
        Config config = Config.getConfig(name);

        if (config == null) return;

        chunkTimestamps.put(name, System.currentTimeMillis());
        pendingChunks.computeIfAbsent(name, k -> new ArrayList<>()).add(packet.data());

        if (packet.index() + 1 == packet.total()) {
            try {
                byte[] allData = Base64.getDecoder().decode(String.join("", pendingChunks.remove(name)));
                chunkTimestamps.remove(name);

                byte[] decompressed = packet.isCompressed()
                        ? ConfigCompressor.decompress(allData)
                        : allData;

                String jsonStr = new String(decompressed, StandardCharsets.UTF_8);
                JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
                ConfigFile file = config.get(packet.configFileIdentifier());

                if (config.debugEnabled()) config.getLogger().info("Received and decompressing {} of {}.json ({} chunks)", name, packet.configFileIdentifier().getNamespace(), packet.total());

                file.decode(json);
            } catch (Exception e) {
                config.getLogger().error("Unable to decompress received config data. Got {}", String.valueOf(e));
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
                Utilitary.LOGGER.warn("Config data timeout. This client might have a desynced {} config data. Proceed with caution.", managerName);
            }
        }
    }
}
