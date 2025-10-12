package dev.proststuff.reconstruct_what.config.instance.value;

import com.mojang.blaze3d.platform.InputConstants;

public class ConfigKeybind extends ConfigString {
    //private final Consumer<Boolean> listener;

    public ConfigKeybind(String name, String defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
        //this.listener = listener;
    }

    public ConfigKeybind(String name, String defaultValue) {
        this(name, defaultValue, false);
    }

    public boolean matches(int keyCode, int scanCode) {
        return value.equalsIgnoreCase(InputConstants.getKey(keyCode, scanCode).getName());
    }
    /*
    public record ConfigKeybindEntry(String keyName, KeybindInteractionType interactionType, long holdTime) {

    }

    public enum KeybindInteractionType {
        PRESS,
        HOLD,
        RELEASE;
    }*/
}
