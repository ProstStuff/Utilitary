package dev.proststuff.utilitary.api.config.serialization.metadata;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.proststuff.utilitary.api.config.serialization.Comment;
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
    public <T> SimpleMetadataType.@NonNull Metadata create(Context<T> context) {
        return new Metadata(context.version());
    }

    @Override
    public JsonElement migrate(JsonElement original) {
        if (original.isJsonObject()) {
            return original.getAsJsonObject().get("version");
        }

        return original;
    }

    @Override
    public Comment comment() {
        return Comment.of("Config version");
    }

    public static DataResult<Metadata> read(Integer version) {
        return DataResult.success(new Metadata(version));
    }

    public record Metadata(int version) implements ConfigMetadata {}
}