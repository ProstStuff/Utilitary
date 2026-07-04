package dev.proststuff.utilitary.api.utility;

import dev.proststuff.utilitary.Utilitary;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    public static final int DEFAULT_CHUNK_SIZE = 32768;

    public static String sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) builder.append(String.format("%02x", b));
            return builder.toString();
        } catch (Exception e) {
            Utilitary.LOGGER.error("Failed to hash data", e);
        }

        return "";
    }


    public static List<byte[]> chunk(byte[] data) {
        List<byte[]> chunks = new ArrayList<>();

        for (int i = 0; i < data.length; i += DEFAULT_CHUNK_SIZE) {
            int end = Math.min(data.length, i + DEFAULT_CHUNK_SIZE);
            chunks.add(Arrays.copyOfRange(data, i, end));
        }

        return chunks;
    }

    public static byte[] mergeChunks(List<byte[]> chunks) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            for (byte[] chunk : chunks) stream.write(chunk);
            return stream.toByteArray();
        } catch (Exception e) {
            Utilitary.LOGGER.error("Failed to merge chunks", e);
        }

        return new byte[0];
    }

    public static boolean exceedsMaxSize(byte[] data, int maxBytes) {
        return data.length > maxBytes;
    }

    public static int getChunkCount(byte[] data, int chunkSize) {
        return (int) Math.ceil((double) data.length / chunkSize);
    }

    public static int getChunkCount(byte[] data) {
        return getChunkCount(data, DEFAULT_CHUNK_SIZE);
    }
}
