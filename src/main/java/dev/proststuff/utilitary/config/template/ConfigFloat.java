package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.utility.config.ConfigCodec;
import dev.proststuff.utilitary.utility.config.ConfigCodecs;

public class ConfigFloat extends ConfigValue<Float> {
    protected final Float min;
    protected final Float max;

    public ConfigFloat(String name, Float value, Float min, Float max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    public ConfigFloat(String name, Float value) {
        this(name, value, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    public ConfigCodec<Float> getCodec() {
        return ConfigCodecs.FLOAT;
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