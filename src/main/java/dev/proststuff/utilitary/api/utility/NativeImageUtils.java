package dev.proststuff.utilitary.api.utility;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NativeImageUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("Utilitary | NativeImageUtils");
    public static final int DEFAULT_CHUNK_SIZE = 32768;

    public static NativeImage read(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            return NativeImage.read(stream);
        } catch (Exception e) {
            LOGGER.error("Failed to read image file", e);
        }

        return null;
    }

    public static NativeImage fromBytes(byte[] data) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(data)) {
            return NativeImage.read(stream);
        } catch (Exception e) {
            LOGGER.error("Failed to decode NativeImage", e);
        }

        return null;
    }

    public static byte[] toBytes(NativeImage image) {
        try {
            Path temp = Files.createTempFile("native_image", ".png");
            image.writeToFile(temp);
            byte[] data = Files.readAllBytes(temp);
            Files.deleteIfExists(temp);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) LOGGER.info("Successfully read {}", image);
            return data;
        } catch (Exception e) {
            LOGGER.error("Failed to encode NativeImage", e);
        }

        return new byte[0];
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
            LOGGER.error("Failed to merge chunks", e);
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

    public static String sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) builder.append(String.format("%02x", b));
            return builder.toString();
        } catch (Exception e) {
            LOGGER.error("Failed to hash data", e);
        }

        return "";
    }
}