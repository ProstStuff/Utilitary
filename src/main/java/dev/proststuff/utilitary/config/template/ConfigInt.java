package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.utility.config.ConfigCodec;
import dev.proststuff.utilitary.utility.config.ConfigCodecs;

public class ConfigInt extends ConfigValue<Integer> {
    protected final int min;
    protected final int max;

    public ConfigInt(String name, Integer value, Integer min, Integer max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    public ConfigInt(String name, Integer value) {
        this(name, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public ConfigCodec<Integer> getCodec() {
        return ConfigCodecs.INT;
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
