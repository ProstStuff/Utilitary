package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

public class ConfigBool extends AbstractConfigValue<Boolean> {
    public ConfigBool(String name, Boolean value, boolean runtimeOnly) {
        super(name, value, runtimeOnly);
    }

    public void toggle() {
        this.set(!this.get());
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            this.set(element.getAsBoolean());
        } catch (Exception e) {
            this.setDefault();
            manager.warn(IFancyLogging.LogType.WARN, "Exception found. Fallback {} value to default.", this.defaultValue.getClass().getName());
        }
    }
}
