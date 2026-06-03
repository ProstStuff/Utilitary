package dev.proststuff.utilitary.api.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.api.config.ConfigFile;
import dev.proststuff.utilitary.api.config.ConfigFileSerializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

public class FileJsonUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("Utilitary | FileJsonUtils");
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
            .setStrictness(Strictness.LENIENT)
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(ConfigFile.class, new ConfigFileSerializer())
            .create();

    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean createDirectories(Path destination) {
        if (!Files.exists(destination.getParent())) {
            try {
                Files.createDirectories(destination.getParent());
                return true;
            } catch (IOException e) {
                LOGGER.error("Unable to create directory {}", destination.getParent());
            }
        }

        return false;
    }

    public static <T> boolean write(Path destination, T data, Class<T> clazz) {
        createDirectories(destination);

        try (Writer writer = new FileWriter(destination.toFile())) {
            GSON.toJson(data, clazz, writer);
            if (Utilitary.isDevelopmentEnvironment()) LOGGER.info("Successfully written {}", destination.getFileName());
            return true;
        } catch (IOException e) {
            LOGGER.error("Unable to write {}", destination.getFileName(), e);
        }

        return false;
    }

    public static <T> T read(Path destination, Class<T> clazz, Supplier<T> fallback) {
        if (!Files.exists(destination)) return fallback.get();

        try (Reader reader = new FileReader(destination.toFile())) {
            T obj = GSON.fromJson(reader, clazz);

            if (obj != null) {
                if (Utilitary.isDevelopmentEnvironment()) LOGGER.info("Successfully read {}", destination.getFileName());
                return obj;
            } else {
                LOGGER.warn("Unable to deserialize {}", destination.getFileName());
                return fallback.get();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to read {}", destination.getFileName(), e);
            return fallback.get();
        }
    }

    public static boolean move(Path source, Path target) {
        createDirectories(target);

        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            LOGGER.error("Unable to move {} to {}", source, target, e);
        }

        return false;
    }

    public static boolean delete(Path destination) {
        try {
            Files.deleteIfExists(destination);
            return true;
        } catch (IOException e) {
            LOGGER.error("Unable to delete {}", destination.getFileName(), e);
        }

        return false;
    }
}
