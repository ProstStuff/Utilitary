package dev.proststuff.utilitary.config.serialization.metadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.proststuff.utilitary.config.serialization.Comment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class SimpleMetadataType implements ConfigMetadataType<SimpleMetadataType.Metadata> {
    public static final Codec<Metadata> CODEC = Codec.INT.comapFlatMap(SimpleMetadataType::read, Metadata::version);
    public static final SimpleMetadataType INSTANCE = new SimpleMetadataType();

    private SimpleMetadataType() {}

    @Override
    public @NonNull Codec<Metadata> codec() {
        return CODEC;
    }

    @Override
    public <T> SimpleMetadataType.@NonNull Metadata create(Identifier fileId, int version, T config) {
        return new Metadata(version);
    }

    @Override
    public Comment comment() {
        return Comment.of(Component.literal("Config Version"));
    }

    public static DataResult<Metadata> read(Integer version) {
        return DataResult.success(new Metadata(version));
    }

    public record Metadata(int version) implements ConfigMetadata {}
}