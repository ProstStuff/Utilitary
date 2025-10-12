package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

public class ConfigString extends ConfigValue<String> {
    public ConfigString(String name, String str, boolean runtimeOnly) {
        super(name, str, ConfigCodecs.STRING, runtimeOnly);
    }

    public ConfigString(String name, String str) {
        this(name, str, false);
    }

    public ConfigString(String name, boolean runtimeOnly) {
        this(name, "", runtimeOnly);
    }

    public ConfigString(String name) {
        this(name, false);
    }

    public boolean isEmpty() {return this.get() == null || get().isEmpty();}
}
