package dev.proststuff.reconstruct_what.platform;

import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.ClientBoundConfigSyncPacket;
import dev.proststuff.reconstruct_what.platform.services.AbstractPlatform;
import net.minecraft.server.level.ServerPlayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public abstract class RWAbstractPlatform extends AbstractPlatform {
    @Override
    public String getModId() {
        return ReconstructWhat.ID;
    }

    public void syncConfigToPlayer(ServerPlayer player, String modId, String configName, JsonElement jsonData) {
        try {
            byte[] compressed = compress(jsonData.toString().getBytes(StandardCharsets.UTF_8));

            final int MAX_CHUNK_SIZE = 512 * 1024;
            int totalChunks = (int) Math.ceil((double) compressed.length / MAX_CHUNK_SIZE);

            info(LogType.ACTION, "Syncing {} config ({} bytes compressed, created {} chunks) to {}",
                    modId, compressed.length, totalChunks, player.getName().getString());

            for (int i = 0; i < totalChunks; i++) {
                int start = i * MAX_CHUNK_SIZE;
                int end = Math.min(compressed.length, start + MAX_CHUNK_SIZE);
                byte[] chunk = Arrays.copyOfRange(compressed, start, end);

                ClientBoundConfigSyncPacket packet = new ClientBoundConfigSyncPacket(
                        modId,
                        configName,
                        i,
                        totalChunks,
                        Base64.getEncoder().encodeToString(chunk),
                        true
                );
                sendToPlayer(player, packet);
            }

        } catch (IOException e) {
            error(LogType.ERROR, "Failed to compress config for '{}': {}", modId, e);
        }
    }

    private static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data);
        }
        return bos.toByteArray();
    }
}