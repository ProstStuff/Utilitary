package dev.proststuff.utilitary.config.v0;

import dev.proststuff.utilitary.api.v0.utility.SimpleIdentifier;
import dev.proststuff.utilitary.config.v0.serialization.metadata.ConfigMetadata;
import org.jspecify.annotations.NonNull;

public record ConfigResult<C, M extends ConfigMetadata>(@NonNull C config, @NonNull M metadata, ConfigStatus status) {
    public ConfigResult(C config, ConfigType<C, M, ?> type, ConfigStatus status) {
        this(config, type.createMetadata(config), status);
    }

    public static <C, M extends ConfigMetadata> ConfigResult<C, M> of(SimpleIdentifier id, ConfigType<C, M, ?> type, ConfigStatus status) {
        C config = type.defaults().create(id);
        return new ConfigResult<>(config, type.createMetadata(config), status);
    }
}