package dev.proststuff.utilitary.api.config.serialization.metadata;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public interface ConfigMetadataType<R extends ConfigMetadata> {
    Codec<R> codec();
    <T> @NonNull R create(Context<T> context);

    record Context<T>(Identifier type, int version, T config) {}
}