package dev.proststuff.utilitary.config.serialization;

import dev.proststuff.utilitary.config.ConfigType;
import dev.proststuff.utilitary.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.util.Accomplishable;
import dev.proststuff.utilitary.util.SimpleIdentifier;

public record ConfigSerializationResult<C, M extends ConfigMetadata>(C config, M metadata, ConfigSerializationStatus status) implements Accomplishable {
    public ConfigSerializationResult(M metadata, C config) {
        this(config, metadata, ConfigSerializationStatus.succeed("Decoded"));
    }

    public ConfigSerializationResult(ConfigType<C, M> type, SimpleIdentifier fileId, Exception exception) {
        C config = type.defaults().create(fileId);
        this(config, type.createMetadata(config), ConfigSerializationStatus.fail(exception));
    }

    @Override
    public boolean isSuccessful() {
        return status.isSuccessful();
    }
}