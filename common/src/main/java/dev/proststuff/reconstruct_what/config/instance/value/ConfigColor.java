package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.awt.*;

public class ConfigColor extends ConfigValue<Color> {
    public ConfigColor(String name, Color defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, ConfigCodecs.COLOR, runtimeOnly);
    }

    public ConfigColor(String name, Color defaultValue) {
        this(name, defaultValue, false);
    }

    public int getRed() { return value.getRed(); }
    public int getGreen() { return value.getGreen(); }
    public int getBlue() { return value.getBlue(); }
    public int getAlpha() { return value.getAlpha(); }
}