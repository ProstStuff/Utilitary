package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

public class ConfigDouble extends AbstractConfigValue<Double> {
    private final double min;
    private final double max;

    public ConfigDouble(String name, double defaultValue, boolean runtimeOnly) {
        this(name, defaultValue, Double.NEGATIVE_INFINITY, java.lang.Double.POSITIVE_INFINITY, false);
    }

    public ConfigDouble(String name, double defaultValue, double min, double max, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
        this.min = min;
        this.max = max;
    }

    @Override
    public Double set(Double value) {
        if (value < this.min) value = this.min;
        if (value > this.max) value = this.max;
        return super.set(value);
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            this.set(element.getAsDouble());
        } catch (Exception e) {
            this.setDefault();
            manager.warn(IFancyLogging.LogType.WARN, "Exception found. Fallback {} value to default.", this.defaultValue.getClass().getName());
        }
    }
}
