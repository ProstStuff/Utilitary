package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

import java.util.UUID;

public class ConfigUUID extends AbstractConfigValue<UUID> {
    public ConfigUUID(String name, UUID defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
    }

    @Override
    public UUID set(UUID value) {
        return super.set(value != null ? value : getDefault());
    }

    public String asString() {
        return get().toString();
    }

    public boolean equalsUUID(UUID other) {
        return get().equals(other);
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        return new JsonPrimitive(this.get().toString());
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            this.set(UUID.fromString(element.getAsString()));
        } catch (Exception e) {
            this.setDefault();
            manager.warn(IFancyLogging.LogType.WARN, "Exception found. Fallback {} value to default.", this.defaultValue.getClass().getName());
        }
    }
}