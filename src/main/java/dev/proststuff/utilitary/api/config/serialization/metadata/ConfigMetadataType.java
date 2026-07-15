package dev.proststuff.utilitary.api.config.serialization.metadata;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import dev.proststuff.utilitary.api.config.serialization.Comment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public interface ConfigMetadataType<R extends ConfigMetadata> {
    @NonNull Codec<R> codec();
    <T> @NonNull R create(Context<T> context);

    default JsonElement migrate(JsonElement original) {
        return original;
    }

    default Comment comment() {
        return Comment.EMPTY;
    }

    record Context<T>(Identifier type, int version, T config) {}
}