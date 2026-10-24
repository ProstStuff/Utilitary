package dev.proststuff.utilitary.config.serialization;

import dev.proststuff.utilitary.config.ConfigType;
import dev.proststuff.utilitary.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.util.SimpleIdentifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ConfigParser {
    static <C, M extends ConfigMetadata> ConfigSerializationResult<C, M> read(ConfigType<C, M> type, SimpleIdentifier fileId, Object element) {
        Path path = type.toPath(fileId);
        return type.format().decode(type, fileId, path, element);
    }

    static <C, M extends ConfigMetadata> ConfigSerializationResult<C, M> read(ConfigType<C, M> type, SimpleIdentifier fileId) {
        Path path = type.toPath(fileId);
        File file = path.toFile();
        if (!file.canRead()) return new ConfigSerializationResult<>(type, fileId, new IOException("Can't read this file"));
        return type.format().decode(type, fileId, path);
    }

    static <C, M extends ConfigMetadata> ConfigSerializationStatus write(ConfigType<C, M> type, SimpleIdentifier fileId, C config, M metadata) {
        Path path = type.toPath(fileId);
        return type.format().encode(type, fileId, path, config, metadata);
    }

    static <C, M extends ConfigMetadata> ConfigSerializationStatus write(ConfigType<C, M> type, SimpleIdentifier fileId, C config) {
        return write(type, fileId, config, type.createMetadata(config));
    }

    static ConfigSerializationStatus erase(ConfigType<?, ?> type, SimpleIdentifier fileId) {
        try {
            Path path = type.toPath(fileId);

            if (Files.exists(path)) {
                Files.delete(path);
                return ConfigSerializationStatus.succeed("Successfully deleted " + path);
            } else {
                return ConfigSerializationStatus.fail("No file is present for it to be deleted");
            }
        } catch (Exception e) {
            return ConfigSerializationStatus.fail(e);
        }
    }
}
