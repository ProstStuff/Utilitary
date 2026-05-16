package dev.proststuff.utilitary.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import dev.proststuff.utilitary.serialization.ConfigFile;
import dev.proststuff.utilitary.serialization.ConfigFileSerializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class UtilitaryJsonUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("Utilitary Json Utils");
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

    public static <T> void write(Path destination, T data, Class<T> clazz) {
        if (!Files.exists(destination.getParent())) {
            try {
                Files.createDirectories(destination.getParent());
            } catch (IOException e) {
                LOGGER.error("Unable to create directory {}", destination.getParent());
                return;
            }
        }

        try (Writer writer = new FileWriter(destination.toFile())) {
            GSON.toJson(data, clazz, writer);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) LOGGER.info("Successfully written {}", destination.getFileName());
        } catch (IOException e) {
            LOGGER.error("Unable to write {}", destination.getFileName(), e);
        }
    }

    public static <T> T read(Path destination, Class<T> clazz, Supplier<T> fallback) {
        if (!Files.exists(destination)) return fallback.get();

        try (Reader reader = new FileReader(destination.toFile())) {
            T obj = GSON.fromJson(reader, clazz);

            if (obj != null) {
                if (FabricLoader.getInstance().isDevelopmentEnvironment()) LOGGER.info("Successfully read {}", destination.getFileName());
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
}
