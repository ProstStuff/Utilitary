package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

public class ConfigLong extends ConfigValue<Long> {
    private final long min;
    private final long max;

    public ConfigLong(String name, long l, long min, long max, boolean runtimeOnly) {
        super(name, l, ConfigCodecs.LONG, runtimeOnly);
        this.min = min;
        this.max = max;
    }

    public ConfigLong(String name, long l, long min, long max) {
        this(name, l, min, max, false);
    }

    public ConfigLong(String name, long l) {
        this(name, l, Long.MIN_VALUE, Long.MAX_VALUE, false);
    }

    public ConfigLong(String name, long l, boolean runtimeOnly) {
        this(name, l, Long.MIN_VALUE, Long.MAX_VALUE, runtimeOnly);
    }

    @Override
    public Long set(Long value) {
        if (value < this.min) value = this.min;
        if (value > this.max) value = this.max;
        return super.set(value);
    }
}
