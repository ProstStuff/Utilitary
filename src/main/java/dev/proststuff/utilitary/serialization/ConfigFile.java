package dev.proststuff.utilitary.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.proststuff.utilitary.serialization.content.field.minecraft.IdentifierConfigField;
import dev.proststuff.utilitary.serialization.impl.ConfigSerializable;
import dev.proststuff.utilitary.utility.UtilitaryJsonUtils;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ConfigFile implements ConfigSerializable {
    private static final Map<Identifier, ConfigFile> CONFIG_FILES = new LinkedHashMap<>();

    public static Map<Identifier, ConfigFile> getConfigFiles() {
        return CONFIG_FILES;
    }

    protected final Identifier identifier;
    protected final List<ConfigSerializable> children = new ArrayList<>();

    public ConfigFile(Identifier identifier) {
        this.identifier = identifier;
        children.add(new IdentifierConfigField("identity", identifier));
        CONFIG_FILES.put(identifier, this);
    }

    public void add(ConfigSerializable field, ConfigSerializable... fields) {
        if (isConfigFile(field)) throw new IllegalArgumentException("ConfigFile cannot add a ConfigFile class as their children!");

        children.add(field);

        for (ConfigSerializable configSerializable : fields) {
            if (isConfigFile(configSerializable)) throw new IllegalArgumentException("ConfigFile cannot add a ConfigFile class as their children!");
            children.add(configSerializable);
        }
    }

    public String getName() {
        return identifier.getPath();
    }

    public Path getDestination() {
        return UtilitaryJsonUtils.getConfigPath().resolve(identifier.getNamespace()).resolve(getName() + ".json");
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        JsonObject serialized = new JsonObject();
        for (ConfigSerializable field : children) {
            serialized.add(field.getName(), field.serialize(context));
        }

        return serialized;
    }

    @Override
    public void deserialize(JsonElement jsonElement, JsonDeserializationContext context) {
        JsonObject serialized = jsonElement.getAsJsonObject();

        for (ConfigSerializable field : children) {
            String name = field.getName();

            if (serialized.has(name)) {
                field.deserialize(serialized.get(name), context);
            }
        }
    }

    public void save() {
        UtilitaryJsonUtils.write(getDestination(), this, ConfigFile.class);
    }

    public void load() {
        UtilitaryJsonUtils.read(getDestination(), ConfigFile.class, () -> this);
    }

    public static boolean isConfigFile(ConfigSerializable configSerializable) {
        return configSerializable instanceof ConfigFile;
    }
}
