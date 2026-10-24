package dev.proststuff.utilitary.config.serialization.format;

import com.mojang.serialization.DynamicOps;
import dev.proststuff.utilitary.config.ConfigType;
import dev.proststuff.utilitary.config.serialization.ConfigSerializationResult;
import dev.proststuff.utilitary.config.serialization.ConfigSerializationStatus;
import dev.proststuff.utilitary.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.util.SimpleIdentifier;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface ConfigFormat<E> {
    @NonNull DynamicOps<E> getOps();

    <C, M extends ConfigMetadata> @NonNull E read(ConfigType<C, M> configType, Path path) throws IOException;
    <C, M extends ConfigMetadata> void write(ConfigType<C, M> configType, Path path, E element) throws IOException;

    @NonNull E getData(E rootElement);
    @NonNull E getMetadata(E rootElement);
    @NonNull Map<ConfigType<?, ?>, Fixer<E>> getAvailableFixers();

    @NonNull String suffix();

    @SuppressWarnings("unchecked")
    default @NonNull <C, M extends ConfigMetadata> ConfigSerializationResult<C, M> decode(ConfigType<C, M> type, SimpleIdentifier fileId, Path path, Object element) {
        try {
            E fixedElement = (E) element;
            if (getAvailableFixers().containsKey(type)) fixedElement = getAvailableFixers().get(type).fix(fileId, fixedElement);
            return type.fileCodec().decode(getOps(), fixedElement).getOrThrow().getFirst();
        } catch (Exception e) {
            return new ConfigSerializationResult<>(type, fileId, e);
        }
    }

    default @NonNull <C, M extends ConfigMetadata> ConfigSerializationResult<C, M> decode(ConfigType<C, M> type, SimpleIdentifier fileId, Path path) {
        try {
            return decode(type, fileId, path, read(type, path));
        } catch (Exception e) {
            return new ConfigSerializationResult<>(type, fileId, e);
        }
    }

    default <C, M extends ConfigMetadata> ConfigSerializationResult<C, M> decode(ConfigType<C, M> type, SimpleIdentifier fileId) {
        return decode(type, fileId, type.toPath(fileId));
    }

    default @NonNull <C, M extends ConfigMetadata> ConfigSerializationStatus encode(ConfigType<C, M> type, SimpleIdentifier fileId, Path path, C config, M metadata) {
        try {
            write(type, path, type.fileCodec().encodeStart(getOps(), new ConfigSerializationResult<>(metadata, config)).getOrThrow());
        } catch (Exception e) {
            return ConfigSerializationStatus.fail(e);
        }

        return ConfigSerializationStatus.succeed("Encoded");
    }

    default <C, M extends ConfigMetadata> ConfigSerializationStatus encode(ConfigType<C, M> type, SimpleIdentifier fileId, C config) {
        return encode(type, fileId, type.toPath(fileId), config, type.createMetadata(config));
    }

    default <C, M extends ConfigMetadata> ConfigSerializationStatus encode(ConfigType<C, M> type, SimpleIdentifier fileId, Path path, C config) {
        return encode(type, fileId, path, config, type.createMetadata(config));
    }

    @FunctionalInterface
    interface Fixer<E> {
        E fix(SimpleIdentifier fileId, E rootElement);
    }
}