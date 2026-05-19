package dev.proststuff.utilitary.api.config.field.value;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;

public class StringConfigField extends ConfigField<String> {
    public StringConfigField(String name, String value) {
        super(name, value, ConfigCodecs.STRING);
    }
}
