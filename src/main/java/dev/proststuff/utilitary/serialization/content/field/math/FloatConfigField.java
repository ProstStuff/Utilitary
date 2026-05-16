package dev.proststuff.utilitary.serialization.content.field.math;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;
import net.minecraft.util.Mth;

public class FloatConfigField extends ConfigField<Float> {
    public float min = Float.MIN_VALUE;
    public float max = Float.MAX_VALUE;

    public FloatConfigField(String name, Float value) {
        super(name, value, ConfigCodecs.FLOAT);
    }

    public FloatConfigField clamp(float min, float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public Float validate(Float value) {
        return Mth.clamp(super.validate(value), min, max);
    }
}
