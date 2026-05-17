package dev.proststuff.utilitary.api.field.value;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;

public class StringConfigField extends ConfigField<String> {
    public StringConfigField(String name, String value) {
        super(name, value, ConfigCodecs.STRING);
    }
}
