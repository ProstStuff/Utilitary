package dev.proststuff.utilitary.api.v1.client.entity;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface UUIDRenderStateHolder {
    default @Nullable UUID utilitary$getUUID() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    default void utilitary$setUUID(@Nullable UUID uuid) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
