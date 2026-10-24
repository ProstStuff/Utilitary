package dev.proststuff.utilitary.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public interface ScreenAutoParentable extends Parentable<Screen> {
    void setParent(Screen parent);
}
