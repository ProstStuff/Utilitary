package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

import java.util.UUID;

public class ConfigUUID extends ConfigValue<UUID> {
    public ConfigUUID(String name, UUID value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<UUID> getCodec() {
        return ConfigCodec.UUID;
    }
}
