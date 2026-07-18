package dev.proststuff.utilitary.config.v0;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.api.v0.utility.SimpleIdentifier;
import dev.proststuff.utilitary.config.v0.serialization.format.ConfigFormat;
import dev.proststuff.utilitary.config.v0.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.config.v0.serialization.metadata.ConfigMetadataType;
import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings({"LoggingSimilarMessage", "unused"})
public interface ConfigManager extends ConfigConstants {
    static <C, M extends ConfigMetadata, F> @NonNull ConfigResult<C, M> load(SimpleIdentifier fileName, ConfigType<C, M, F> type) {
        return load(fileName, type, type.format().create(fileName, type));
    }

    static <C, M extends ConfigMetadata, F> @NonNull ConfigResult<C, M> load(SimpleIdentifier fileName, ConfigType<C, M, F> type, F formatSettings) {
        if (!type.codec().canSerialize()) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} type can't perform deserialization", type.id());
            return ConfigResult.of(fileName, type, ConfigStatus.NO_CODEC);
        }

        Path path = toPath(fileName, type.format());

        try {
            if (Files.notExists(path)) return ConfigResult.of(fileName, type, ConfigStatus.NOT_EXIST);
            ConfigMetadataType<M> metadataType = type.metadata();

            JsonObject root = type.format().read(path, formatSettings).getAsJsonObject();
            JsonElement jsonMetadata;

            if (root.get(METADATA_KEY) != null) {
                jsonMetadata = root.remove(METADATA_KEY);
            } else {
                jsonMetadata = new JsonPrimitive(0);
            }

            M metadata = metadataType.codec().parse(JsonOps.INSTANCE, jsonMetadata).getOrThrow();
            JsonElement data;

            if (root.has(DATA_KEY)) {
                data = root.remove(DATA_KEY);
            } else {
                data = root;
            }

            List<ConfigType.Migration> migrations = type.migrations();
            int version = metadata.version();

            while (version < type.version()) {
                ConfigType.Migration migration = migrations.get(version);

                if (migration == null) throw new IllegalStateException("Missing migration for version " + version);
                data = migration.migrate(new ConfigType.Migration.Context(fileName, data, version));
                version ++;
            }

            ConfigCodec<C> codec = type.codec();
            return new ConfigResult<>(
                    codec.getCodec()
                            .parse(JsonOps.INSTANCE, data)
                            .getOrThrow(),
                    metadata,
                    ConfigStatus.SUCCESS
            );
        } catch (Exception e) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] Unable to read {}, assume corrupted file:", fileName, e);
            return new ConfigResult<>(
                    type.defaults().create(fileName),
                    type,
                    ConfigStatus.ERROR
            );
        }
    }

    static <C> ConfigStatus save(SimpleIdentifier fileName, ConfigType<C, ?, ?> type, ConfigResult<C, ?> result) {
        return save(fileName, type, result.config());
    }

    static <C, F> ConfigStatus save(SimpleIdentifier fileName, ConfigType<C, ?, F> type, C config) {
        return save(fileName, type, type.format().create(fileName, type), config);
    }

    static <C, M extends ConfigMetadata, F> ConfigStatus save(SimpleIdentifier fileName, ConfigType<C, M, F> type, F formatSettings, C config) {
        if (!type.codec().canSerialize()) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] {} type can't perform serialization", type.id());
            return ConfigStatus.NO_CODEC;
        }

        Path path = toPath(fileName, type.format());

        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            root.add(METADATA_KEY, type.metadata().codec().encodeStart(JsonOps.INSTANCE, type.createMetadata(config)).getOrThrow());
            root.add(DATA_KEY, type.codec().getCodec().encodeStart(JsonOps.INSTANCE, config).getOrThrow());

            type.format().write(path, root, formatSettings);
            return ConfigStatus.SUCCESS;
        } catch (Exception e) {
            Utilitary.LOGGER.warn("[UTILITARY CONFIG] Unable to write {}", fileName, e);
            return ConfigStatus.ERROR;
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
}