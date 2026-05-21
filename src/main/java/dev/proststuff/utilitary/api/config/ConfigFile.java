package dev.proststuff.utilitary.api.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.impl.ConfigFileChild;
import dev.proststuff.utilitary.api.config.impl.ConfigSerializable;
import dev.proststuff.utilitary.api.utility.FileJsonUtils;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ConfigFile implements ConfigSerializable {
    private static final Map<SimpleIdentifier, ConfigFile> CONFIG_FILES = new LinkedHashMap<>();

    protected final SimpleIdentifier identifier;
    protected final List<ConfigFileChild> children = new ArrayList<>();
    protected SaveStatus saveStatus = SaveStatus.UNSAVED;
    protected LoadStatus loadStatus = LoadStatus.UNLOADED;

    public ConfigFile(SimpleIdentifier identifier) {
        this.identifier = identifier;
        CONFIG_FILES.put(identifier, this);
    }

    public ConfigFile(Identifier identifier) {
        this(SimpleIdentifier.fromIdentifier(identifier));
    }

    public ConfigFile(String namespace, String name) {
        this(new SimpleIdentifier(namespace, name));
    }

    public void add(ConfigFileChild field, ConfigFileChild... fields) {
        if (isConfigFile(field)) throw new IllegalArgumentException("ConfigFile cannot add a ConfigFile class as their children!");

        children.add(field);

        for (ConfigFileChild configField : fields) {
            if (isConfigFile(configField)) throw new IllegalArgumentException("ConfigFile cannot add a ConfigFile class as their children!");
            children.add(configField);
        }
    }

    public String getName() {
        return identifier.path();
    }

    public Path getDestination() {
        return FileJsonUtils.getConfigPath().resolve(identifier.namespace()).resolve(getName() + ".json");
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        JsonObject serialized = new JsonObject();

        for (ConfigFileChild field : children) {
            serialized.add(field.getName(), field.serialize(context));
        }

        serialized.add("identity", ConfigCodecs.SIMPLE_IDENTIFIER.encode(identifier, context));
        return serialized;
    }

    @Override
    public void deserialize(JsonElement jsonElement, JsonDeserializationContext context) {
        JsonObject serialized = jsonElement.getAsJsonObject();

        for (ConfigFileChild field : children) {
            String name = field.getName();

            if (serialized.has(name)) {
                field.deserialize(serialized.get(name), context);
            }
        }

        this.loadStatus = LoadStatus.DESERIALIZATION_LOADED;
    }

    public void save() {
        if (FileJsonUtils.write(getDestination(), this, ConfigFile.class)) {
            this.saveStatus = SaveStatus.SAVED;
        }
    }

    public void load() {
        if (FileJsonUtils.read(getDestination(), ConfigFile.class, () -> null) != null) {
            this.loadStatus = LoadStatus.DISK_LOADED;
        }
    }

    public boolean delete() {
        if (FileJsonUtils.delete(getDestination())) {
            saveStatus = SaveStatus.UNSAVED;
            loadStatus = LoadStatus.UNLOADED;
            return remove(identifier) != null;
        }

        return false;
    }


    public static Map<SimpleIdentifier, ConfigFile> getConfigFiles() {
        return CONFIG_FILES;
    }

    public static ConfigFile remove(SimpleIdentifier identifier) {
        return CONFIG_FILES.remove(identifier);
    }

    public static boolean isConfigFile(Object object) {
        return object instanceof ConfigFile;
    }

    public enum SaveStatus {
        UNSAVED,
        SAVED
    }

    public enum LoadStatus {
        UNLOADED,
        DISK_LOADED,
        DESERIALIZATION_LOADED,
    }
}