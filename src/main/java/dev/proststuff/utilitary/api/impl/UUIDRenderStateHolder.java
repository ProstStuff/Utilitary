package dev.proststuff.utilitary.api.impl;

import java.util.UUID;

public interface UUIDRenderStateHolder {
    default UUID utilitary$getUUID() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    default void utilitary$setUUID(UUID uuid) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
