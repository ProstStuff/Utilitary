package dev.proststuff.reconstruct_what.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.adapter.ConfigHierarchyAdapter;
import dev.proststuff.reconstruct_what.config.adapter.ConfigValueAdapter;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.config.instance.ConfigGroup;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.config.instance.value.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class ConfigHelper {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AbstractConfigValue.class, new ConfigValueAdapter<>())
            .registerTypeHierarchyAdapter(ICanConfigure.class, new ConfigHierarchyAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    public static ConfigBool b(String name, boolean defaultBool) {
        return new ConfigBool(name, defaultBool, false);
    }

    public static ConfigDouble d(String name, double defaultDouble, double min, double max) {
        return new ConfigDouble(name, defaultDouble, min, max, false);
    }
    public static ConfigDouble d(String name, double defaultDouble, double max) {
        return new ConfigDouble(name, defaultDouble, 0, max, false);
    }
    public static ConfigDouble d(String name, double defaultDouble) {
        return new ConfigDouble(name, defaultDouble, false);
    }

    public static <E extends Enum<E>> ConfigEnum<E> e(String name, Class<E> enumClass, E defaultEnum) {
        return new ConfigEnum<>(name, enumClass, defaultEnum, false);
    }

    public static ConfigInt i(String name, int defaultInteger, int min, int max) {
        return new ConfigInt(name, defaultInteger, min, max, false);
    }
    public static ConfigInt i(String name, int defaultInteger, int max) {
        return new ConfigInt(name, defaultInteger, 0, max, false);
    }
    public static ConfigInt i(String name, int defaultInteger) {
        return new ConfigInt(name, defaultInteger, false);
    }

    public static <L> ConfigList<L> a(String name) {
        return new ConfigList<>(name, false);
    }
    @SafeVarargs
    public static <L> ConfigList<L> a(String name, L... elements) {
        return new ConfigList<>(name, List.of(elements), false);
    }

    public static ConfigLong l(String name, long defaultLong, long min, long max) {
        return new ConfigLong(name, defaultLong, min, max, false);
    }
    public static ConfigLong l(String name, long defaultLong, long max) {
        return new ConfigLong(name, defaultLong, 0, max, false);
    }
    public static ConfigLong l(String name, long defaultLong) {
        return new ConfigLong(name, defaultLong, false);
    }

    public static <K, V> ConfigMap<K, V> m(String name) {
        return new ConfigMap<>(name, false);
    }
    public static <K, V> ConfigMap<K, V> m(String name, Map<K, V> map) {
        return new ConfigMap<>(name, map, false);
    }

    public static ConfigResourceLocation r(String name, ResourceLocation defaultLocation) {
        return new ConfigResourceLocation(name, defaultLocation);
    }

    public static ConfigString s(String name, String value) {
        return new ConfigString(name, value, false);
    }

    public static ConfigUUID u(String name, UUID defaultUUID) {
        return new ConfigUUID(name, defaultUUID, false);
    }

    public static ConfigGroup g(String name) {
        return new ConfigGroup(name);
    }

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

    // TODO: Add startup to the config
    /**
     * Configuration type.
     */
    public enum ConfigType {
        CLIENT,
        COMMON,
        SERVER,
        @Deprecated
        STARTUP;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
