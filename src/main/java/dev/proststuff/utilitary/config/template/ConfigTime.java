package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

import java.time.Duration;

public class ConfigTime extends ConfigValue<Long> {
    public ConfigTime(String name, Long ms) {
        super(name, ms);
    }

    @Override
    public ConfigCodec<Long> getCodec() {
        return ConfigCodec.TIME;
    }

    @Override
    public boolean set(Long ms) {
        if (ms < 0) ms = 0L;
        return super.set(ms);
    }

    public Duration getAsDuration() {return Duration.ofMillis(get());}
    public int getAsTick() {return (int) (get() / 50L);}
}
