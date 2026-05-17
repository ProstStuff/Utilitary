package dev.proststuff.utilitary.api.field.value.math;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;
import net.minecraft.util.Mth;

public class DoubleConfigField extends ConfigField<Double> {
    public double min = Double.MIN_VALUE;
    public double max = Double.MAX_VALUE;

    public DoubleConfigField(String name, Double value) {
        super(name, value, ConfigCodecs.DOUBLE);
    }

    public DoubleConfigField clamp(double min, double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public Double validate(Double value) {
        return Mth.clamp(super.validate(value), min, max);
    }
}
