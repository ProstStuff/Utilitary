package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.utility.config.ConfigCodec;
import dev.proststuff.utilitary.utility.config.ConfigCodecs;

import java.util.UUID;

public class ConfigUUID extends ConfigValue<UUID> {
    protected ConfigUUID(String name, UUID value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<UUID> getCodec() {
        return ConfigCodecs.UUID;
    }
}
