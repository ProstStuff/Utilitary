package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

public class ConfigInt extends AbstractConfigValue<Integer> {
    private final int min;
    private final int max;

    public ConfigInt(String name, int defaultValue, boolean runtimeOnly) {
        this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }

    public ConfigInt(String name, int defaultValue, int min, int max, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer set(Integer value) {
        if (value < this.min) value = this.min;
        if (value > this.max) value = this.max;
        return super.set(value);
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            this.set(element.getAsInt());
        } catch (Exception e) {
            this.setDefault();
            manager.warn(IFancyLogging.LogType.WARN, "Exception found. Fallback {} value to default.", this.defaultValue.getClass().getName());
        }
    }
}
