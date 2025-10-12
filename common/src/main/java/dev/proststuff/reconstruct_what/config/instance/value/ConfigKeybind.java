package dev.proststuff.reconstruct_what.config.instance.value;

import com.mojang.blaze3d.platform.InputConstants;

public class ConfigKeybind extends ConfigString {
    public ConfigKeybind(String name, String keybind, boolean runtimeOnly) {
        super(name, keybind, runtimeOnly);
    }

    public ConfigKeybind(String name, String keybind) {
        this(name, keybind, false);
    }

    public boolean matches(int keyCode, int scanCode) {return value.equalsIgnoreCase(InputConstants.getKey(keyCode, scanCode).getName());}
}
