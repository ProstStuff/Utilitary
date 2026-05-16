package dev.proststuff.utilitary.serialization.content.field.math;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;
import dev.proststuff.utilitary.utility.Color;

public class ColorConfigField extends ConfigField<Color> {
    public ColorConfigField(String name, int value) {
        super(name, new Color(value), ConfigCodecs.COLOR);
    }
}