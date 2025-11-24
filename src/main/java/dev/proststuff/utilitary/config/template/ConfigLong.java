package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

public class ConfigLong extends ConfigValue<Long> {
    protected final Long min;
    protected final Long max;
    
    public ConfigLong(String name, Long value, Long min, Long max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }
    
    public ConfigLong(String name, Long value) {
        this(name, value, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    public ConfigCodec<Long> getCodec() {
        return ConfigCodec.LONG;
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