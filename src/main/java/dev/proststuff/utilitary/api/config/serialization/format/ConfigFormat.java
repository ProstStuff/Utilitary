package dev.proststuff.utilitary.api.config.serialization.format;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.api.config.ConfigType;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Path;

public interface ConfigFormat<S> {
    @NonNull JsonElement read(Path path, S formatSettings) throws IOException;
    void write(Path path, JsonElement element, S formatSettings) throws IOException;

    <C, M extends ConfigMetadata> @NonNull S create(SimpleIdentifier fileName, ConfigType<C, M, S> type);
    @NonNull String suffix();
}