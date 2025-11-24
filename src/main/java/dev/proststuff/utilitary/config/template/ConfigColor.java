package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

import java.awt.*;

//TODO: Support Alpha color
public class ConfigColor extends ConfigValue<Color> {
    public ConfigColor(String name, Color value) {
        super(name, value);
    }

    public ConfigColor(String name, String hex) {
        this(name, Color.decode(hex));
    }

    public ConfigColor(String name, int color) {
        this(name, String.format("#%06X", color & 0xFFFFFF));
    }

    @Override
    public ConfigCodec<Color> getCodec() {
        return ConfigCodec.COLOR;
    }

    public int getRed() {return value.getRed();}
    public int getGreen() {return value.getGreen();}
    public int getBlue() {return value.getBlue();}
}
