package dev.proststuff.utilitary.api.v1.client.gui;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.Identifier;

public interface GroupableElement extends GuiEventListener {
    Identifier getGroup();
}
