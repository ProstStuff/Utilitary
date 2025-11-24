package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

public class ConfigString extends ConfigValue<String> {
    public ConfigString(String name, String value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<String> getCodec() {
        return ConfigCodec.STRING;
    }

    public boolean isEmpty() {
        return get().isEmpty();
    }
}
