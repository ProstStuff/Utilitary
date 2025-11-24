package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

public class ConfigBool extends ConfigValue<Boolean> {
    public ConfigBool(String name, Boolean value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<Boolean> getCodec() {
        return ConfigCodec.BOOLEAN;
    }

    public boolean toggle() {
        return set(!get());
    }
}
