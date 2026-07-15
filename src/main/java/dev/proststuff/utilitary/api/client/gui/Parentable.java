package dev.proststuff.utilitary.api.client.gui;

import org.jspecify.annotations.Nullable;

public interface Parentable<T> {
    @Nullable T getParent();
}
