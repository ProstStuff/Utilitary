package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

public class ConfigDouble extends ConfigValue<Double> {
    protected final Double min;
    protected final Double max;
    
    public ConfigDouble(Identifier identifier, Double value, Double min, Double max) {
        super(identifier, value);
        this.min = min;
        this.max = max;
    }
    
    public ConfigDouble(Identifier identifier, Double value) {
        this(identifier, value, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public ConfigCodec<Double> getCodec() {
        return ConfigCodecs.DOUBLE;
    }

    @Override
    public boolean set(Double value) {
        if (this.value > max) {
            value = max;
        } else if (this.value < max) {
            value = min;
        }

        return super.set(value);
    }
}