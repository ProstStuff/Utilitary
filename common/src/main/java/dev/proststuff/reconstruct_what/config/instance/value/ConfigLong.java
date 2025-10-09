package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

public class ConfigLong extends AbstractConfigValue<Long> {
    private final long min;
    private final long max;

    public ConfigLong(String name, long defaultValue, boolean runtimeOnly) {
        this(name, defaultValue, Long.MIN_VALUE, Long.MAX_VALUE, runtimeOnly);
    }

    public ConfigLong(String name, long defaultValue, long min, long max, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
        this.min = min;
        this.max = max;
    }

    @Override
    public Long set(Long value) {
        if (value < min) value = min;
        if (value > max) value = max;
        return super.set(value);
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            this.set(element.getAsLong());
        } catch (Exception e) {
            this.setDefault();
            manager.warn(IFancyLogging.LogType.WARN, "Exception found. Fallback {} value to default.", this.defaultValue.getClass().getName());
        }
    }
}
