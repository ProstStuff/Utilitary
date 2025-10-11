package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

public class ConfigDouble extends ConfigValue<Double> {
    private final double min;
    private final double max;

    public ConfigDouble(String name, double defaultValue, double min, double max, boolean runtimeOnly) {
        super(name, defaultValue, ConfigCodecs.DOUBLE, runtimeOnly);
        this.min = min;
        this.max = max;
    }

    public ConfigDouble(String name, double defaultValue, double min, double max) {
        this(name, defaultValue, min, max, false);
    }

    public ConfigDouble(String name, double defaultValue) {
        this(name, defaultValue, Double.NEGATIVE_INFINITY, java.lang.Double.POSITIVE_INFINITY, false);
    }

    public ConfigDouble(String name, double defaultValue, boolean runtimeOnly) {
        this(name, defaultValue, Double.NEGATIVE_INFINITY, java.lang.Double.POSITIVE_INFINITY, runtimeOnly);
    }

    @Override
    public Double set(Double value) {
        if (value < this.min) value = this.min;
        if (value > this.max) value = this.max;
        return super.set(value);
    }
}
