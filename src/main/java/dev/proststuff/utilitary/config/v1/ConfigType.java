package dev.proststuff.utilitary.config.v1;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.api.v1.utility.SimpleIdentifier;
import dev.proststuff.utilitary.config.v1.serialization.Comment;
import dev.proststuff.utilitary.config.v1.serialization.format.ConfigFormat;
import dev.proststuff.utilitary.config.v1.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.config.v1.serialization.metadata.ConfigMetadataType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record ConfigType<C, M extends ConfigMetadata, F>(
                @NonNull Identifier id, int version,
                @NonNull ConfigCodec<C> codec,
                @NonNull ConfigDefaults<C> defaults,
                @NonNull List<Migration> migrations,
                @NonNull ConfigMetadataType<M> metadata,
                @NonNull ConfigFormat<F> format,
                @NonNull Comment comments
) {
    public ConfigType(
            @NonNull Identifier id, int version,
            @NonNull ConfigCodec<C> codec,
            @NonNull ConfigDefaults<C> defaults,
            @NonNull List<Migration> migrations,
            @NonNull ConfigMetadataType<M> metadata,
            @NonNull ConfigFormat<F> format
    ) {
        this(id, version, codec, defaults, migrations, metadata, format, Comment.of());
    }

    public ConfigType(
            @NonNull Identifier id, int version,
            @NonNull ConfigCodec<C> codec,
            @NonNull ConfigDefaults<C> defaults,
            @NonNull ConfigMetadataType<M> metadata,
            @NonNull ConfigFormat<F> format,
            @NonNull Comment comments
    ) {
        this(id, version, codec, defaults, new ArrayList<>(), metadata, format, comments);
    }

    public ConfigType(
            @NonNull Identifier id, int version,
            @NonNull ConfigCodec<C> codec,
            @NonNull ConfigDefaults<C> defaults,
            @NonNull ConfigMetadataType<M> metadata,
            @NonNull ConfigFormat<F> format
    ) {
        this(id, version, codec, defaults, new ArrayList<>(), metadata, format, Comment.of());
    }

    public M createMetadata(C config) {
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