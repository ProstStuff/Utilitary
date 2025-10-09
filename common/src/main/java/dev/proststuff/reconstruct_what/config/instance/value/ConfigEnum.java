package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

public class ConfigEnum<E extends Enum<E>> extends AbstractConfigValue<E> {
    private final Class<E> enumClass;

    public ConfigEnum(String name, Class<E> enumClass, E defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
        this.enumClass = enumClass;
    }

    public void next() {
        E[] values = enumClass.getEnumConstants();
        int index = get().ordinal();
        int next = (index + 1) % values.length;
        set(values[next]);
    }

    public void set(String name) {
        for (E val : enumClass.getEnumConstants()) {
            if (val.name().equalsIgnoreCase(name)) {
                set(val);
                return;
            }
        }

        ReconstructWhat.LOG.error("Unknown enum value of ConfigEnum: {} for {}", name, getName());
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        return new JsonPrimitive(this.get().name());
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            this.set(element.getAsString());
        } catch (Exception e) {
            this.setDefault();
            manager.warn(IFancyLogging.LogType.WARN, "Exception found. Fallback {} value to default.", this.defaultValue.getClass().getName());
        }
    }
}
