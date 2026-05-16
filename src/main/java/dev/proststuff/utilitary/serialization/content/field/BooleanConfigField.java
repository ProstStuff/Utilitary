package dev.proststuff.utilitary.serialization.content.field;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;

public class BooleanConfigField extends ConfigField<Boolean> {
    public BooleanConfigField(String name, Boolean value) {
        super(name, value, ConfigCodecs.BOOLEAN);
    }

    public boolean toggle() {
        set(!get());
        return get();
    }
}
