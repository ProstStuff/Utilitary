package dev.proststuff.utilitary.api.config.field.value;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;

public class BooleanConfigField extends ConfigField<Boolean> {
    public BooleanConfigField(String name, Boolean value) {
        super(name, value, ConfigCodecs.BOOLEAN);
    }

    public boolean toggle() {
        set(!get());
        return get();
    }
}
