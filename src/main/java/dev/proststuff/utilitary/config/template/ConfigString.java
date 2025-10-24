package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.utility.config.ConfigCodec;
import dev.proststuff.utilitary.utility.config.ConfigCodecs;

public class ConfigString extends ConfigValue<String> {
    public ConfigString(String name, String value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<String> getCodec() {
        return ConfigCodecs.STRING;
    }

    public boolean isEmpty() {
        return get().isEmpty();
    }
}
