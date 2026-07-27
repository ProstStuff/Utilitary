package dev.proststuff.utilitary.api.v0.util;

import com.mojang.blaze3d.platform.NativeImage;
import dev.proststuff.utilitary.Utilitary;
import io.netty.util.internal.UnstableApi;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@UnstableApi
public final class NativeImageUtils {
    private NativeImageUtils() {}

    public static NativeImage read(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            return NativeImage.read(stream);
        } catch (Exception e) {
            Utilitary.LOGGER.error("[UTILITARY IMAGE] Failed to read image file", e);
        }

        return null;
    }

    public static NativeImage fromBytes(byte[] data) throws IOException {
        if (Utilitary.CONFIG.debugLogging()) Utilitary.LOGGER.info("Decoding bytes to image");
        return NativeImage.read(new ByteArrayInputStream(data));
    }

    public static byte @Nullable [] toBytes(NativeImage image) {
        if (Utilitary.CONFIG.debugLogging()) Utilitary.LOGGER.info("Encoding {} NativeImage ({}, {})", image, image.getWidth(), image.getHeight());

        Path temp;
        byte[] bytes = null;

        try {
            temp = Files.createTempFile("native_image_temp", ".png");
        } catch (IOException e) {
            Utilitary.LOGGER.error("[UTILITARY IMAGE] Unable to create a temp file to encode NativeImage", e);
            return null;
        }

        try {
            image.writeToFile(temp);
            bytes = Files.readAllBytes(temp);
        } catch (IOException e) {
            Utilitary.LOGGER.error("[UTILITARY IMAGE] Failed to encode NativeImage", e);
        }

        try {
            Files.deleteIfExists(temp);
        } catch (IOException e) {
            Utilitary.LOGGER.error("[UTILITARY IMAGE] Failed to delete a temp file", e);
        }

        return bytes;
    }
}