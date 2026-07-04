package dev.proststuff.utilitary.api.config.serialization.metadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import org.jspecify.annotations.NonNull;

public class VersionOnlyConfigMetadataType implements ConfigMetadataType<VersionOnlyConfigMetadataType.Metadata> {
    public static final Codec<Metadata> CODEC = Codec.INT.comapFlatMap(VersionOnlyConfigMetadataType::read, Metadata::version);

    @Override
    public Codec<Metadata> codec() {
        return CODEC;
    }

    @Override
    public <T> VersionOnlyConfigMetadataType.@NonNull Metadata create(Context<T> context) {
        return new Metadata(context.version());
    }

    public static DataResult<Metadata> read(Integer version) {
        return DataResult.success(new Metadata(version));
    }

    public record Metadata(int version) implements ConfigMetadata {}
}