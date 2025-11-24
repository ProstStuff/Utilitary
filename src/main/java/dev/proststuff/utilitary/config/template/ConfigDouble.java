package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

public class ConfigDouble extends ConfigValue<Double> {
    protected final Double min;
    protected final Double max;
    
    public ConfigDouble(String name, Double value, Double min, Double max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }
    
    public ConfigDouble(String name, Double value) {
        this(name, value, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public ConfigCodec<Double> getCodec() {
        return ConfigCodec.DOUBLE;
    }

    @Override
    public void changed() {
        if (this.value > max) {
            this.value = max;
        } else if (this.value < max) {
            this.value = min;
        }
        
        super.changed();
    }
}