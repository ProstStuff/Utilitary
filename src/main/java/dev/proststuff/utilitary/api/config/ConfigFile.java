package dev.proststuff.utilitary.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import dev.proststuff.utilitary.api.config.impl.ConfigFileChild;
import dev.proststuff.utilitary.api.config.impl.ConfigSerializable;
import dev.proststuff.utilitary.api.utility.FileJsonUtils;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.*;

public abstract class ConfigFile implements ConfigSerializable {
    private static final Map<SimpleIdentifier, ConfigFile> CONFIG_FILES = new LinkedHashMap<>();

    protected @NonNull SimpleIdentifier identifier;
    protected final List<ConfigFileChild> children = new ArrayList<>();
    protected final Map<String, String> migrator = new HashMap<>();
    protected SaveStatus saveStatus = SaveStatus.UNSAVED;
    protected LoadStatus loadStatus = LoadStatus.UNLOADED;

    public ConfigFile(@NonNull SimpleIdentifier identifier) {
        this.identifier = identifier;
        if (CONFIG_FILES.put(identifier, this) != null) {
            throw new IllegalStateException("Duplicated config identifier: " + identifier);
        }
    }

    public ConfigFile(Identifier identifier) {
        this(SimpleIdentifier.fromIdentifier(identifier));
    }

    public ConfigFile(String namespace, String name) {
        this(new SimpleIdentifier(namespace, name));
    }

    public void migrateField(String oldField, String newField) {
        migrator.put(oldField, newField);
    }

    public void migrateField(String oldField, ConfigField<?> newField) {
        migrateField(oldField, newField.getName());
    }

    public void add(ConfigFileChild field, ConfigFileChild... fields) {
        if (isConfigFile(field)) throw new IllegalArgumentException("ConfigFile cannot add a ConfigFile class as their children!");

        children.add(field);
        field.setConfigFile(this);

        for (ConfigFileChild configField : fields) {
            if (isConfigFile(configField)) throw new IllegalArgumentException("ConfigFile cannot add a ConfigFile class as their children!");
            children.add(configField);
            configField.setConfigFile(this);
        }
    }

    public String getName() {
        return identifier.path();
    }

    public Path getFilePath() {
        return toFilePath(identifier);
    }

    @Override
    public JsonElement serialize() {
        JsonObject serialized = new JsonObject();

        for (ConfigFileChild field : children) {
            serialized.add(field.getName(), field.serialize());
        }

        serialized.add("identity", ConfigCodecs.SIMPLE_IDENTIFIER.encode(identifier));
        return serialized;
    }

    @Override
    public void deserialize(JsonElement jsonElement) {
        JsonObject serialized = jsonElement.getAsJsonObject();

        for (ConfigFileChild field : children) {
            String name = field.getName();

            if (serialized.has(name) || migrator.containsValue(field.getName())) {
                field.deserialize(serialized.get(name));
            }
        }

        this.loadStatus = LoadStatus.DESERIALIZATION_LOADED;
    }

    public void markDirty() {
        saveStatus = SaveStatus.UNSAVED;
    }

    public void save() {
        if (saveStatus != SaveStatus.SAVED && FileJsonUtils.write(getFilePath(), this, ConfigFile.class)) {
            saveStatus = SaveStatus.SAVED;
        }
    }

    public void load() {
        if (FileJsonUtils.read(getFilePath(), ConfigFile.class, () -> null) != null) {
            loadStatus = LoadStatus.DISK_LOADED;
        }
    }

    public boolean migrate(SimpleIdentifier target) throws IllegalStateException {
        if (identifier.equals(target)) return true;

        if (canMigrate(target)) {
            SimpleIdentifier oldIdentifier = identifier;

            Path oldDestination = getFilePath();
            Path newDestination = toFilePath(target);
            if (!FileJsonUtils.move(oldDestination, newDestination)) return false;

            identifier = target;
            CONFIG_FILES.remove(oldIdentifier);
            CONFIG_FILES.put(target, this);
        } else {
            throw new IllegalStateException("Cannot migrate to " + target + " as it already exists!");
        }

        return false;
    }

    public boolean delete() {
        if (FileJsonUtils.delete(getFilePath())) {
            saveStatus = SaveStatus.UNSAVED;
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

    public static boolean canMigrate(SimpleIdentifier migrateTo) {
        return !CONFIG_FILES.containsKey(migrateTo);
    }

    public static Path toFilePath(SimpleIdentifier identifier) {
        return FileJsonUtils.getConfigPath().resolve(identifier.namespace()).resolve(identifier.path() + ".json");
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