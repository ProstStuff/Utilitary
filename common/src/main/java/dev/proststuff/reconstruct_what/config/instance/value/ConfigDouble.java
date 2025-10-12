package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

public class ConfigDouble extends ConfigValue<Double> {
    private final double min;
    private final double max;

    public ConfigDouble(String name, double d, double min, double max, boolean runtimeOnly) {
        super(name, d, ConfigCodecs.DOUBLE, runtimeOnly);
        this.min = min;
        this.max = max;
    }

    public ConfigDouble(String name, double d, double min, double max) {
        this(name, d, min, max, false);
    }

    public ConfigDouble(String name, double d) {
        this(name, d, Double.NEGATIVE_INFINITY, java.lang.Double.POSITIVE_INFINITY, false);
    }

    public ConfigDouble(String name, double d, boolean runtimeOnly) {
        this(name, d, Double.NEGATIVE_INFINITY, java.lang.Double.POSITIVE_INFINITY, runtimeOnly);
    }

    @Override
    public Double set(Double value) {
        if (value < this.min) value = this.min;
        if (value > this.max) value = this.max;
        return super.set(value);
    }
}
