package dev.proststuff.utilitary.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface Parentable<T> {
    @Nullable T getParent();
}
