package dev.proststuff.utilitary.api.config;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.api.config.serialization.format.ConfigFormat;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.api.config.serialization.metadata.ConfigMetadataType;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.NavigableMap;
import java.util.function.Supplier;

public record ConfigType<C, M extends ConfigMetadata, F>(
                Identifier id, int version,
                @NonNull ConfigCodec<C> codec,
                @NonNull ConfigDefaults<C> defaults,
                @Nullable NavigableMap<Integer, Migration> migrations,
                ConfigMetadataType<M> metadata,
                @NonNull ConfigFormat<F> format
) {
    public static <C, M extends ConfigMetadata, F> ConfigType<C, M, F> of(
            Identifier id, int version,
            @NonNull ConfigCodec<C> codec,
            @NonNull ConfigDefaults<C> defaults,
            Supplier<NavigableMap<Integer, Migration>> migrations,
            ConfigMetadataType<M> metadata,
            ConfigFormat<F> fileCodec
    ) {
        return new ConfigType<>(id, version, codec, defaults, migrations.get(), metadata, fileCodec);
    }

    public M toMetadata(C config) {
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