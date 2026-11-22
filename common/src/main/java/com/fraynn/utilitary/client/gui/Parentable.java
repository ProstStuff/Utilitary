package com.fraynn.utilitary.client.gui;

import org.jspecify.annotations.Nullable;

public interface Parentable<T> {
    @Nullable T getParent();
}
