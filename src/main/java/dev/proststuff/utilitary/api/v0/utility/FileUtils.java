package dev.proststuff.utilitary.api.v0.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

public interface FileUtils {
    int DEFAULT_CHUNK_SIZE = 32768;

    static String sha256(byte[] data) {
        return HexFormat.of().formatHex(data);
    }

    static List<byte[]> chunk(byte[] data, int chunkSize) {
        List<byte[]> chunks = new ArrayList<>();

        for (int i = 0; i < data.length; i += chunkSize) {
            int end = Math.min(data.length, i + chunkSize);
            chunks.add(Arrays.copyOfRange(data, i, end));
        }

        return chunks;
    }

    static List<byte[]> chunk(byte[] data) {
        return chunk(data, DEFAULT_CHUNK_SIZE);
    }

    static byte[] mergeChunks(List<byte[]> chunks) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        for (byte[] chunk : chunks) stream.write(chunk);
        return stream.toByteArray();
    }

    static int getChunkCount(byte[] data, int chunkSize) {
        return (data.length + chunkSize - 1) / chunkSize;
    }

    static int getChunkCount(byte[] data) {
        return getChunkCount(data, DEFAULT_CHUNK_SIZE);
    }
}