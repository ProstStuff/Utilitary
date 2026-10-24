package dev.proststuff.utilitary.config.serialization.metadata;

import com.mojang.serialization.Codec;
import dev.proststuff.utilitary.config.serialization.Comment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public interface ConfigMetadataType<R extends ConfigMetadata> {
    @NonNull Codec<R> codec();
    <T> @NonNull R create(Identifier type, int version, T config);

    default Comment comment() {
        return Comment.EMPTY;
    }
}