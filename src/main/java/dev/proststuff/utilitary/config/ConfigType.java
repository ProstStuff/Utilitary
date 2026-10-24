package dev.proststuff.utilitary.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.proststuff.utilitary.config.serialization.Comment;
import dev.proststuff.utilitary.config.serialization.ConfigSerializationResult;
import dev.proststuff.utilitary.config.serialization.format.ConfigFormat;
import dev.proststuff.utilitary.config.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.config.serialization.metadata.ConfigMetadataType;
import dev.proststuff.utilitary.util.SimpleIdentifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;

public record ConfigType<C, M extends ConfigMetadata>(
                @NonNull Identifier id, int version,
                @NonNull Codec<ConfigSerializationResult<C, M>> fileCodec,
                @NonNull ConfigDefaults<C> defaults,
                @NonNull ConfigMetadataType<M> metadata,
                @NonNull ConfigFormat<?> format,
                @NonNull Comment comments
) {
    public M createMetadata(C config) {
        return metadata.create(id, version, config);
    }

    public Path toPath(SimpleIdentifier id) {
        return FabricLoader.getInstance().getConfigDir().resolve(id.namespace()).resolve(id.path() + "." + format.suffix());
    }

    @FunctionalInterface
    public interface ConfigDefaults<T> {
        @Nullable T create(SimpleIdentifier fileId);
    }

    public static <C, M extends ConfigMetadata> Codec<ConfigSerializationResult<C, M>> fileCodec(Codec<M> metadataCodec, Codec<C> dataCodec) {
        return RecordCodecBuilder.create(inst -> inst.group(
                metadataCodec.fieldOf(ConfigConstants.METADATA_KEY).forGetter(ConfigSerializationResult::metadata),
                dataCodec.fieldOf(ConfigConstants.DATA_KEY).forGetter(ConfigSerializationResult::config)
        ).apply(inst, ConfigSerializationResult::new));
    }
}