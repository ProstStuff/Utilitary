package dev.proststuff.utilitary.api.config;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadataType;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.NavigableMap;
import java.util.function.Supplier;

public record ConfigType<T, R extends ConfigMetadata, M extends ConfigMetadataType<R>>(
        Identifier id, int version,
        @NonNull ConfigCodec<T> codec,
        @NonNull ConfigDefaults<T> defaults,
        @Nullable NavigableMap<Integer, Migration> migrations,
        M metadata
) {
    public static <T, R extends ConfigMetadata, M extends ConfigMetadataType<R>> ConfigType<T, R, M> of(
            Identifier id, int version,
            @NonNull ConfigCodec<T> codec,
            @NonNull ConfigDefaults<T> defaults,
            Supplier<NavigableMap<Integer, Migration>> migrations,
            M metadata
    ) {
        return new ConfigType<>(id, version, codec, defaults, migrations.get(), metadata);
    }

    public R toMetadata(T config) {
        return metadata.create(new ConfigMetadataType.Context<>(id, version, config));
    }

    public interface ConfigDefaults<T> {
        T create(SimpleIdentifier fileId);
    }

    public interface Migration {
        JsonElement migrate(Context context);

        record Context(
                SimpleIdentifier fileId,
                JsonElement data,
                int fromVersion
        ) {}
    }
}