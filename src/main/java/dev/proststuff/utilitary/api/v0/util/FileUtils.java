package dev.proststuff.utilitary.api.v0.util;

import io.netty.util.internal.UnstableApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

@UnstableApi
public final class FileUtils {
    public static final int DEFAULT_CHUNK_SIZE = 32768;

    private FileUtils() {}

    public static String sha256(byte[] data) {
        return HexFormat.of().formatHex(data);
    }

    public static List<byte[]> chunk(byte[] data, int chunkSize) {
        List<byte[]> chunks = new ArrayList<>();

        for (int i = 0; i < data.length; i += chunkSize) {
            int end = Math.min(data.length, i + chunkSize);
            chunks.add(Arrays.copyOfRange(data, i, end));
        }

        return chunks;
    }

    public static List<byte[]> chunk(byte[] data) {
        return chunk(data, DEFAULT_CHUNK_SIZE);
    }

    public static byte[] mergeChunks(List<byte[]> chunks) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        for (byte[] chunk : chunks) stream.write(chunk);
        return stream.toByteArray();
    }

    public static int getChunkCount(byte[] data, int chunkSize) {
        return (data.length + chunkSize - 1) / chunkSize;
    }

    public static int getChunkCount(byte[] data) {
        return getChunkCount(data, DEFAULT_CHUNK_SIZE);
    }
}