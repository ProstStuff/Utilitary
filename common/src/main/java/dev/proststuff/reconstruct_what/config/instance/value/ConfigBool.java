package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;

public class ConfigBool extends ConfigValue<Boolean> {
    public ConfigBool(String name, Boolean bool, boolean runtimeOnly) {
        super(name, bool, ConfigCodecs.BOOL, runtimeOnly);
    }

    public ConfigBool(String name, Boolean bool) {
        super(name, bool, ConfigCodecs.BOOL, false);
    }

    public boolean toggle() {return this.set(!this.get());}
}
