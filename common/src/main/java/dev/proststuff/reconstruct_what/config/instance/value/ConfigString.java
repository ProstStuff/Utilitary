package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;

public class ConfigString extends AbstractConfigValue<String> {
    public ConfigString(String name, String defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
    }

    public boolean isEmpty() {
        return this.get() == null || get().isEmpty();
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        this.set(element.getAsString());
    }
}
