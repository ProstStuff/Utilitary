package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

public class ConfigFloat extends ConfigValue<Float> {
    protected final Float min;
    protected final Float max;

    public ConfigFloat(Identifier identifier, Float value, Float min, Float max) {
        super(identifier, value);
        this.min = min;
        this.max = max;
    }

    public ConfigFloat(Identifier identifier, Float value) {
        this(identifier, value, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    public ConfigCodec<Float> getCodec() {
        return ConfigCodecs.FLOAT;
    }

    @Override
    public boolean set(Float value) {
        if (this.value > max) {
            value = max;
        } else if (this.value < max) {
            value = min;
        }

        return super.set(value);
    }
}