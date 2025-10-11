package dev.proststuff.reconstruct_what.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.adapter.ConfigHierarchyAdapter;
import dev.proststuff.reconstruct_what.config.adapter.ConfigValueAdapter;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHelper {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ConfigValue.class, new ConfigValueAdapter<>())
            .registerTypeHierarchyAdapter(ICanConfigure.class, new ConfigHierarchyAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    public static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            ReconstructWhat.LOG.error(e.getMessage());
        }
    }

    public static Path getConfigDirectoryFor(String modId) {
        return Path.of("config", modId);
    }

    public static Path getServerConfigDirectoryFor(MinecraftServer server, String modId) {
        return server.getWorldPath(LevelResource.DATAPACK_DIR).getParent().resolve("serverconfig").resolve(modId);
    }

    public static Path getConfigPath(String modId, ConfigHolder holder) {
        return Path.of("config", modId, holder.getName() + ".json");
    }

    /**
     * Configuration type.
     */
    public enum ConfigType {
        CLIENT,
        COMMON,
        SERVER,
        // Each mod must manually call `.loadSpecific()`.
        STARTUP;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
