package dev.proststuff.utilitary.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public interface GroupableElement extends GuiEventListener {
    Identifier getGroup();
}
