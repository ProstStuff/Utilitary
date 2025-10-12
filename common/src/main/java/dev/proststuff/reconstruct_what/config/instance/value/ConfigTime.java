package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.time.Duration;

public class ConfigTime extends ConfigValue<Long> {
    public ConfigTime(String name, Long defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, ConfigCodecs.TIME, runtimeOnly);
    }

    @Override
    public Long set(Long newValue) {
        if (newValue < 0) newValue = 0L;
        return super.set(newValue);
    }

    public Duration getAsDuration() {
        return Duration.ofMillis(get());
    }

    public int getAsTick() {
        return (int) (get() / 50L);
    }
}
