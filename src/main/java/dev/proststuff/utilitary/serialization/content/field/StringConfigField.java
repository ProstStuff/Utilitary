package dev.proststuff.utilitary.serialization.content.field;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;

public class StringConfigField extends ConfigField<String> {
    public StringConfigField(String name, String value) {
        super(name, value, ConfigCodecs.STRING);
    }
}
