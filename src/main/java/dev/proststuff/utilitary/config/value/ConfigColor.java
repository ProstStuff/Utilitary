package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

import java.awt.*;

//TODO: Support Alpha color
public class ConfigColor extends ConfigValue<Color> {
    public ConfigColor(Identifier identifier, Color value) {
        super(identifier, value);
    }

    public ConfigColor(Identifier identifier, String hex) {
        this(identifier, Color.decode(hex));
    }

    public ConfigColor(Identifier identifier, int color) {
        this(identifier, String.format("#%06X", color & 0xFFFFFF));
    }

    @Override
    public ConfigCodec<Color> getCodec() {
        return ConfigCodecs.COLOR;
    }

    public int getRed() {return value.getRed();}
    public int getGreen() {return value.getGreen();}
    public int getBlue() {return value.getBlue();}
}
