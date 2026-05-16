package dev.proststuff.utilitary.serialization.content;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.proststuff.utilitary.serialization.ConfigFile;
import dev.proststuff.utilitary.serialization.impl.ConfigSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigGroup implements ConfigSerializable {
    protected final String name;
    public Map<String, ConfigSerializable> children = new LinkedHashMap<>();

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
    public JsonElement serialize(JsonSerializationContext context) {
        JsonObject serialized = new JsonObject();

        for (ConfigSerializable child : children.values()) {
            serialized.add(child.getName(), child.serialize(context));
        }

        return serialized;
    }

    @Override
    public void deserialize(JsonElement jsonElement, JsonDeserializationContext context) {
        JsonObject serialized = jsonElement.getAsJsonObject();

        for (ConfigSerializable child : children.values()) {
            String name = child.getName();

            if (serialized.has(name)) {
                child.deserialize(serialized.get(name), context);
            }
        }
    }
}
