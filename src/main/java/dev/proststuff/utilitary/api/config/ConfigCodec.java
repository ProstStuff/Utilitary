package dev.proststuff.utilitary.api.config;

import com.mojang.serialization.Codec;
import dev.proststuff.utilitary.Utilitary;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public record ConfigCodec<T>(@Nullable Codec<T> codec, @Nullable StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    public static <T> ConfigCodec<T> of(Codec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return new ConfigCodec<>(codec, streamCodec);
    }

    public static <T> ConfigCodec<T> of(Codec<T> codec) {
        return of(codec, null);
    }

    public static <T> ConfigCodec<T> of(StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return of(null, streamCodec);
    }

    public static <T> ConfigCodec<T> of() {
        return of(null, null);
    }

    public boolean canSerialize() {
        return codec != null && (Utilitary.CONFIG == null || !Utilitary.CONFIG.safeMode());
    }

    public boolean canSynchronize() {
        return streamCodec != null;
    }

    public @NonNull Codec<T> getOrThrowCodec() throws UnsupportedOperationException {
        if (codec == null) {
            throw new UnsupportedOperationException("No defined codec is present");
        }

        return codec;
    }

    public @NonNull StreamCodec<RegistryFriendlyByteBuf, T> getOrThrowStreamCodec() throws UnsupportedOperationException {
        if (streamCodec == null) {
            throw new UnsupportedOperationException("No defined stream codec is present");
        }

        return streamCodec;
    }
}