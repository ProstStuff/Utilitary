package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

import java.time.Duration;

public class ConfigTime extends ConfigValue<Long> {
    public ConfigTime(Identifier identifier, Long ms) {
        super(identifier, ms);
    }

    @Override
    public ConfigCodec<Long> getCodec() {
        return ConfigCodecs.TIME;
    }

    @Override
    public boolean set(Long ms) {
        if (ms < 0) ms = 0L;
        return super.set(ms);
    }

    public Duration getAsDuration() {return Duration.ofMillis(get());}
    public int getAsTick() {return (int) (get() / 50L);}
}
