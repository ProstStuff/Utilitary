package com.fraynn.utilitary.client.gui;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.Identifier;

public interface GroupableElement extends GuiEventListener {
    Identifier getGroup();
}
