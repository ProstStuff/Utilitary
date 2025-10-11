package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.util.UUID;

public class ConfigUUID extends ConfigValue<UUID> {
    public ConfigUUID(String name, UUID defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, ConfigCodecs.UUID, runtimeOnly);
    }

    public ConfigUUID(String name, UUID defaultValue) {
        this(name, defaultValue, false);
    }
}