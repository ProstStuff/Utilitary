package dev.proststuff.utilitary.api.field.value;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;

public class BooleanConfigField extends ConfigField<Boolean> {
    public BooleanConfigField(String name, Boolean value) {
        super(name, value, ConfigCodecs.BOOLEAN);
    }

    public boolean toggle() {
        set(!get());
        return get();
    }
}
