package dev.proststuff.reconstruct_what.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.ClientBoundConfigSyncPacket;
import dev.proststuff.reconstruct_what.config.ConfigManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class ClientConfigHelper {
    private static final Map<String, List<String>> pendingChunks = new HashMap<>();
    private static final Map<String, Long> chunkTimestamps = new HashMap<>();
    private static final long TIMEOUT_MS = 30_000;

    static {
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ConfigSyncCleaner");
            t.setDaemon(true);
            return t;
        }).scheduleAtFixedRate(ClientConfigHelper::cleanupExpired, 10, 10, TimeUnit.SECONDS);
    }

    public static void receiveChunk(ClientBoundConfigSyncPacket packet) {
        String modId = packet.modId();

        chunkTimestamps.put(modId, System.currentTimeMillis());

        pendingChunks.computeIfAbsent(modId, k -> new ArrayList<>()).add(packet.data());

        if (packet.index() + 1 == packet.total()) {
            try {
                byte[] allData = Base64.getDecoder().decode(String.join("", pendingChunks.remove(modId)));
                chunkTimestamps.remove(modId);

                byte[] decompressed = packet.isCompressed()
                        ? decompress(allData)
                        : allData;

                String jsonStr = new String(decompressed, StandardCharsets.UTF_8);
                JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();

                ReconstructWhat.LOG.info("Received and decompressed '{}' config ({} chunks)",
                        modId, packet.total());

                ConfigManager manager = ConfigManager.getManager(modId);
                if (manager != null && manager.getServer() != null) {
                    manager.getServer().applySynced(json, manager);
                }
            } catch (Exception e) {
                ReconstructWhat.LOG.error("Failed to decode config for '{}': {}", modId, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static byte[] decompress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data))) {
            gis.transferTo(bos);
        }
        return bos.toByteArray();
    }

    private static void cleanupExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> it = chunkTimestamps.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            if (now - entry.getValue() > TIMEOUT_MS) {
                String modId = entry.getKey();
                it.remove();
                pendingChunks.remove(modId);
                ReconstructWhat.LOG.warn("Timeout: Discarded incomplete sync for '{}'", modId);
            }
        }
    }
}
