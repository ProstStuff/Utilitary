package dev.proststuff.utilitary.api.v1.client.gui;

import org.jspecify.annotations.Nullable;

public interface Parentable<T> {
    @Nullable T getParent();
}
