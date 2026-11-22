package com.fraynn.utilitary.config.serialization;

import com.fraynn.utilitary.config.ConfigType;
import com.fraynn.utilitary.config.serialization.metadata.ConfigMetadata;
import com.fraynn.utilitary.util.SimpleIdentifier;

public record ConfigSerializationResult<C, M extends ConfigMetadata>(C config, M metadata, ConfigSerializationStatus status) {
    public ConfigSerializationResult(M metadata, C config) {
        this(config, metadata, ConfigSerializationStatus.succeed("Decoded"));
    }

    public ConfigSerializationResult(ConfigType<C, M> type, SimpleIdentifier fileId, Exception exception) {
        C config = type.defaults().create(fileId);
        this(config, type.createMetadata(config), ConfigSerializationStatus.fail(exception));
    }

    public boolean isSuccessful() {
        return status.isSuccessful();
    }
}