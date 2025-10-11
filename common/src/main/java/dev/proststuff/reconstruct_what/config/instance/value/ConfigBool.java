package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;

public class ConfigBool extends ConfigValue<Boolean> {
    public ConfigBool(String name, Boolean value, boolean runtimeOnly) {
        super(name, value, ConfigCodecs.BOOL, runtimeOnly);
    }

    public ConfigBool(String name, Boolean value) {
        super(name, value, ConfigCodecs.BOOL, false);
    }

    public void toggle() {
        this.set(!this.get());
    }
}
