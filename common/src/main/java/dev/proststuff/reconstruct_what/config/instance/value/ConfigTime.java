package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.time.Duration;

public class ConfigTime extends ConfigValue<Long> {
    public ConfigTime(String name, Long ms, boolean runtimeOnly) {
        super(name, ms, ConfigCodecs.TIME, runtimeOnly);
    }

    public ConfigTime(String name, Long ms) {
        this(name, ms, false);
    }

    @Override
    public Long set(Long ms) {
        if (ms < 0) ms = 0L;
        return super.set(ms);
    }

    public Duration getAsDuration() {return Duration.ofMillis(get());}
    public int getAsTick() {return (int) (get() / 50L);}
}