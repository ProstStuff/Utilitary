package dev.proststuff.utilitary.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.api.config.serialization.format.ConfigFormat;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadataType;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NavigableMap;

@SuppressWarnings({"LoggingSimilarMessage", "unused"})
public interface ConfigManager {
    static <C, M extends ConfigMetadata, F> @NonNull Result<C, M> load(SimpleIdentifier fileName, ConfigType<C, M, F> type) {
        return load(fileName, type, type.format().create(fileName, type));
    }

    static <C, M extends ConfigMetadata, F> @NonNull Result<C, M> load(SimpleIdentifier fileName, ConfigType<C, M, F> type, F formatSettings) {
        if (!type.codec().canSerialize()) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} type can't perform deserialization", type.id());
            return Result.of(type.defaults().create(fileName), type, Status.NO_CODEC);
        }

        Path path = toPath(fileName, type.format());

        try {
            if (Files.notExists(path)) return Result.of(type.defaults().create(fileName), type, Status.NOT_EXIST);
            ConfigMetadataType<M> metadataType = type.metadata();

            JsonObject root = type.format().read(path, formatSettings).getAsJsonObject();
            JsonElement jsonMetadata;

            if (root.get(ConfigConstants.METADATA_KEY) != null) {
                jsonMetadata = root.remove(ConfigConstants.METADATA_KEY);
            } else {
                jsonMetadata = new JsonPrimitive(0);
            }

            M metadata = metadataType.codec().parse(JsonOps.INSTANCE, jsonMetadata).getOrThrow();
            JsonElement data;

            if (root.has(ConfigConstants.DATA_KEY)) {
                data = root.remove(ConfigConstants.DATA_KEY);
            } else {
                data = root;
            }

            NavigableMap<Integer, ConfigType.Migration> migrations = type.migrations();
            int version = metadata.version();

            while (migrations != null && version < type.version()) {
                ConfigType.Migration migration = migrations.get(version);

                if (migration == null) throw new IllegalStateException("Missing migration from version " + version);
                data = migration.migrate(new ConfigType.Migration.Context(fileName, data, version));
                version ++;
            }

            return Result.of(type.codec().getOrThrowCodec().parse(JsonOps.INSTANCE, data).getOrThrow(), metadata, Status.SUCCESS);
        } catch (Exception e) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] Unable to write {}, assume corrupted file:", fileName, e);
            return Result.of(type.defaults().create(fileName), type, Status.ERROR);
        }
    }

    static <C> Status save(SimpleIdentifier fileName, ConfigType<C, ?, ?> type, Result<C, ?> result) {
        return save(fileName, type, result.config);
    }

    static <C, F> Status save(SimpleIdentifier fileName, ConfigType<C, ?, F> type, C config) {
        return save(fileName, type, type.format().create(fileName, type), config);
    }

    static <C, M extends ConfigMetadata, F> Status save(SimpleIdentifier fileName, ConfigType<C, M, F> type, F formatSettings, C config) {
        if (!type.codec().canSerialize()) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} type can't perform serialization", type.id());
            return Status.NO_CODEC;
        }

        Path path = toPath(fileName, type.format());

        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            root.add(ConfigConstants.METADATA_KEY, type.metadata().codec().encodeStart(JsonOps.INSTANCE, type.toMetadata(config)).getOrThrow());
            root.add(ConfigConstants.DATA_KEY, type.codec().getOrThrowCodec().encodeStart(JsonOps.INSTANCE, config).getOrThrow());

            type.format().write(path, root, formatSettings);
            return Status.SUCCESS;
        } catch (Exception e) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] Unable to write {}", fileName, e);
            return Status.ERROR;
        }
    }

    static boolean delete(SimpleIdentifier fileName, ConfigType<?, ?, ?> type) {
        try {
            Path path = toPath(fileName, type.format());
            if (Files.exists(path)) Files.delete(path);
            return true;
        } catch (Exception e) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] Unable to delete {}", fileName, e);
            return false;
        }
    }

    static Path toPath(SimpleIdentifier id, ConfigFormat<?> format) {
        return FabricLoader.getInstance().getConfigDir().resolve(id.namespace()).resolve(id.path() + "." + format.suffix());
    }

    record Result<C, M extends ConfigMetadata>(@NonNull C config, @NonNull M metadata, Status status) {
        public static <C, M extends ConfigMetadata> Result<C, M> of(C config, M metadata, Status status) {
            return new Result<>(config, metadata, status);
        }

        public static <C, M extends ConfigMetadata> Result<C, M> of(C config, ConfigType<C, M, ?> type, Status status) {
            return of(config, type.toMetadata(config), status);
        }
    }

    enum Status {
        SUCCESS,
        NOT_EXIST,
        ERROR,
        NO_CODEC,
    }
}