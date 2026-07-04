package dev.proststuff.utilitary.api.config;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadataType;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

@SuppressWarnings("LoggingSimilarMessage")
public interface ConfigManager {
    Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .serializeNulls()
            .setStrictness(Strictness.LENIENT)
            .enableComplexMapKeySerialization()
            .create();

    Map<Identifier, ConfigType<?, ?, ?>> TYPES = new HashMap<>();

    static ConfigType<?, ?, ?> get(Identifier id) {
        return TYPES.get(id);
    }

    static <C extends ConfigType<?, ?, ?>> C register(C type) {
        if (TYPES.put(type.id(), type) != null) {

            throw new IllegalStateException("Duplicate config type: " + type.id());
        }

        return type;
    }

    @SuppressWarnings("UnusedReturnValue")
    static <T, R extends ConfigMetadata, M extends ConfigMetadataType<R>> boolean save(SimpleIdentifier id, ConfigType<T, R, M> type, T config) {
        if (!TYPES.containsKey(type.id())) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} is unregistered, and might cause problems because of it. Please register it using ConfigManager.register()", id);
        }

        if (!type.codec().canSerialize()) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} type can't perform serialization", type.id());
            return false;
        }

        Path path = toPath(id);

        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            root.add(ConfigConstants.METADATA_KEY, type.metadata().codec().encodeStart(JsonOps.INSTANCE, type.toMetadata(config)).getOrThrow());
            root.add(ConfigConstants.DATA_KEY, type.codec().getOrThrowCodec().encodeStart(JsonOps.INSTANCE, config).getOrThrow());

            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(root, writer);
            }

            return true;
        } catch (Exception e) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] Unable to load {}", id, e);
            return false;
        }
    }

    static <T, R extends ConfigMetadata, M extends ConfigMetadataType<R>> T load(SimpleIdentifier id, ConfigType<T, R, M> type) {
        if (!TYPES.containsKey(type.id())) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} is unregistered, and might cause problems because of it. Please register it using ConfigManager.register()", id);
        }

        if (!type.codec().canSerialize()) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} type can't perform deserialization", type.id());
            return type.defaults().create(id);
        }

        Path path = toPath(id);

        try {
            if (Files.notExists(path)) {
                T defaults = type.defaults().create(id);
                save(id, type, defaults);
                return defaults;
            }

            try (Reader reader = Files.newBufferedReader(path)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                R metadata = type.metadata().codec().parse(JsonOps.INSTANCE, root.get(ConfigConstants.METADATA_KEY)).getOrThrow();

                JsonElement data = root.get(ConfigConstants.DATA_KEY);

                NavigableMap<Integer, ConfigType.Migration> migrations = type.migrations();
                int version = metadata.version();

                while (migrations != null && version < type.version()) {
                    ConfigType.Migration migration = migrations.get(version);

                    if (migration == null) throw new IllegalStateException("Missing migration from version " + version);
                    data = migration.migrate(new ConfigType.Migration.Context(id, data, version));
                    version ++;
                }

                return type.codec().getOrThrowCodec().parse(JsonOps.INSTANCE, data).getOrThrow();
            }
        } catch (Exception e) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] Unable to load {}, assume corrupted file:", id, e);

            T defaults = type.defaults().create(id);
            save(id, type, defaults);
            return defaults;
        }
    }

    static Path toPath(SimpleIdentifier id) {
        return FabricLoader.getInstance().getConfigDir().resolve(id.namespace()).resolve(id.path() + ".json");
    }
}