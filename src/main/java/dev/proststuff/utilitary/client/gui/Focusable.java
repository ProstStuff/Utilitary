package dev.proststuff.utilitary.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface Focusable {
    boolean isFocusable();
}
