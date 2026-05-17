package dev.proststuff.utilitary.api.field.value.math;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;
import net.minecraft.util.Mth;

public class LongConfigField extends ConfigField<Long> {
    public long min = Long.MIN_VALUE;
    public long max = Long.MAX_VALUE;

    public LongConfigField(String name, Long value) {
        super(name, value, ConfigCodecs.LONG);
    }

    public LongConfigField(String name, Long value, long min, long max) {
        super(name, value, ConfigCodecs.LONG);
        this.min = min;
        this.max = max;
    }

    public LongConfigField clamp(long min, long max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public Long validate(Long value) {
        return Mth.clamp(super.validate(value), min, max);
    }
}
