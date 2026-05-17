package dev.proststuff.utilitary.api.field.value.math;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;
import net.minecraft.util.Mth;

public class IntegerConfigField extends ConfigField<Integer> {
    public int min = Integer.MIN_VALUE;
    public int max = Integer.MAX_VALUE;

    public IntegerConfigField(String name, Integer value) {
        super(name, value, ConfigCodecs.INTEGER);
    }

    public IntegerConfigField(String name, Integer value, int min, int max) {
        super(name, value, ConfigCodecs.INTEGER);
        this.min = min;
        this.max = max;
    }

    public IntegerConfigField clamp(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public Integer validate(Integer value) {
        return Mth.clamp(super.validate(value), min, max);
    }
}
