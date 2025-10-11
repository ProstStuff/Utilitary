package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

public class ConfigInt extends ConfigValue<Integer> {
    private final int min;
    private final int max;

    public ConfigInt(String name, int defaultValue, int min, int max, boolean runtimeOnly) {
        super(name, defaultValue, ConfigCodecs.INT, runtimeOnly);
        this.min = min;
        this.max = max;
    }

    public ConfigInt(String name, int defaultValue, int min, int max) {
        this(name, defaultValue, min, max, false);
    }

    public ConfigInt(String name, int defaultValue) {
        this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }

    public ConfigInt(String name, int defaultValue, boolean runtimeOnly) {
        this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, runtimeOnly);
    }

    @Override
    public Integer set(Integer value) {
        if (value < this.min) value = this.min;
        if (value > this.max) value = this.max;
        return super.set(value);
    }
}
