package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.utility.config.ConfigCodec;
import dev.proststuff.utilitary.utility.config.ConfigCodecs;

public class ConfigBool extends ConfigValue<Boolean> {
    public ConfigBool(String name, Boolean value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<Boolean> getCodec() {
        return ConfigCodecs.BOOLEAN;
    }

    public boolean toggle() {
        return set(!get());
    }
}
