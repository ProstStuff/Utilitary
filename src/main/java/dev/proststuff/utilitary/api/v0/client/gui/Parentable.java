package dev.proststuff.utilitary.api.v0.client.gui;

import org.jspecify.annotations.Nullable;

public interface Parentable<T> {
    @Nullable T getParent();
}
