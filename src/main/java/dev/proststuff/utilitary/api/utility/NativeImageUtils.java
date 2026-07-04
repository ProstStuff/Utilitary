package dev.proststuff.utilitary.api.utility;

import com.mojang.blaze3d.platform.NativeImage;
import dev.proststuff.utilitary.Utilitary;
import net.fabricmc.loader.api.FabricLoader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class NativeImageUtils {
    public static NativeImage read(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            return NativeImage.read(stream);
        } catch (Exception e) {
            Utilitary.LOGGER.error("Failed to read image file", e);
        }

        return null;
    }

    public static NativeImage fromBytes(byte[] data) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(data)) {
            return NativeImage.read(stream);
        } catch (Exception e) {
            Utilitary.LOGGER.error("Failed to decode NativeImage", e);
        }

        return null;
    }

    public static byte[] toBytes(NativeImage image) {
        try {
            Path temp = Files.createTempFile("native_image", ".png");
            image.writeToFile(temp);
            byte[] data = Files.readAllBytes(temp);
            Files.deleteIfExists(temp);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) Utilitary.LOGGER.info("Successfully read {}", image);
            return data;
        } catch (Exception e) {
            Utilitary.LOGGER.error("Failed to encode NativeImage", e);
        }

        return new byte[0];
    }
}