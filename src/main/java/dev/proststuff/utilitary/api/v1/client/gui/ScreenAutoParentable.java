package dev.proststuff.utilitary.api.v1.client.gui;

import net.minecraft.client.gui.screens.Screen;

public interface ScreenAutoParentable extends Parentable<Screen> {
    void setParent(Screen parent);
}
