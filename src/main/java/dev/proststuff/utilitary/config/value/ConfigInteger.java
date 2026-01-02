package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

public class ConfigInteger extends ConfigValue<Integer> {
    protected final int min;
    protected final int max;

    public ConfigInteger(Identifier identifier, Integer value, Integer min, Integer max) {
        super(identifier, value);
        this.min = min;
        this.max = max;
    }

    public ConfigInteger(Identifier identifier, Integer value) {
        this(identifier, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public ConfigCodec<Integer> getCodec() {
        return ConfigCodecs.INT;
    }

    @Override
    public boolean set(Integer value) {
        if (this.value > max) {
            value = max;
        } else if (this.value < max) {
            value = min;
        }

        return super.set(value);
    }
}
