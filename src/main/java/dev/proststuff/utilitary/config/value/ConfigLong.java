package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

public class ConfigLong extends ConfigValue<Long> {
    protected final Long min;
    protected final Long max;
    
    public ConfigLong(Identifier identifier, Long value, Long min, Long max) {
        super(identifier, value);
        this.min = min;
        this.max = max;
    }
    
    public ConfigLong(Identifier identifier, Long value) {
        this(identifier, value, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    public ConfigCodec<Long> getCodec() {
        return ConfigCodecs.LONG;
    }

    @Override
    public boolean set(Long value) {
        if (this.value > max) {
            value = max;
        } else if (this.value < max) {
            value = min;
        }

        return super.set(value);
    }
}