package dev.proststuff.utilitary.api.client.gui;

import net.minecraft.client.gui.screens.Screen;

public interface ScreenAutoParentable extends Parentable<Screen> {
    void setParent(Screen parent);
}
