package dev.proststuff.utilitary.api.config.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.proststuff.utilitary.api.config.ConfigFile;
import dev.proststuff.utilitary.api.config.impl.ConfigFileChild;
import dev.proststuff.utilitary.api.config.impl.ConfigSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigGroup implements ConfigFileChild {
    protected final String name;
    protected ConfigFile configFile;
    public final Map<String, ConfigSerializable> children = new LinkedHashMap<>();

    public ConfigGroup(String name) {
        this.name = name;
    }

    public ConfigGroup add(ConfigSerializable field, ConfigSerializable... fields) {
        if (ConfigFile.isConfigFile(field)) throw new IllegalArgumentException("ConfigFile cannot be a field of a ConfigFile class!");

        children.put(field.getName(), field);

        for (ConfigSerializable configSerializable : fields) {
            if (ConfigFile.isConfigFile(configSerializable)) throw new IllegalArgumentException("ConfigFile cannot be a field of a ConfigFile class!");
            children.put(field.getName(), configSerializable);
        }

        return this;
    }

    public ConfigSerializable get(String name) {
        return children.get(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }

    @Override
    public ConfigFile getConfigFile() {
        return configFile;
    }

    @Override
    public JsonElement serialize() {
        JsonObject serialized = new JsonObject();

        for (ConfigSerializable child : children.values()) {
            serialized.add(child.getName(), child.serialize());
        }

        return serialized;
    }

    @Override
    public void deserialize(JsonElement jsonElement) {
        JsonObject serialized = jsonElement.getAsJsonObject();

        for (ConfigSerializable child : children.values()) {
            String name = child.getName();

            if (serialized.has(name)) {
                child.deserialize(serialized.get(name));
            }
        }
    }
}
