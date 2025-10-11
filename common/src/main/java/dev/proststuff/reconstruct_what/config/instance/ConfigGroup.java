package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigGroup implements ICanConfigure<ICanConfigure<?>> {
    private final String name;
    private final Map<String, ICanConfigure<?>> entries = new LinkedHashMap<>();

    public ConfigGroup(String groupName) {
        this.name = groupName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public ConfigGroup add(ICanConfigure<?> entry) {
        entries.put(entry.getName(), entry);

        return this;
    }

    public ICanConfigure<?> get() {
        return this;
    }

    public ICanConfigure<?> get(String name) {
        return entries.get(name);
    }

    public Map<String, ICanConfigure<?>> getEntries() {
        return entries;
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        JsonObject json = new JsonObject();

        for (var entry : entries.entrySet()) {
            ICanConfigure<?> config = entry.getValue();

            if (config instanceof ConfigGroup group) {
                json.add(entry.getKey(), group.serialize(manager));
            } else if (config instanceof ConfigValue<?> value && !value.isRuntimeOnly()) {
                json.add(entry.getKey(), value.serialize(manager));
            }
        }

        return json;
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        if (!element.isJsonObject()) return;
        JsonObject json = element.getAsJsonObject();

        for (var entry : entries.entrySet()) {
            String key = entry.getKey();
            if (!json.has(key)) continue;

            ICanConfigure<?> config = entry.getValue();
            JsonElement child = json.get(key);

            if (config instanceof ConfigGroup group) {
                group.deserialize(child, manager);
            } else if (config instanceof ConfigValue<?> value) {
                value.deserialize(child, manager);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("ConfigGroup{name='%s', entries=%d}", name, entries.size());
    }

    @Override
    public void loaded(ConfigManager configManager) {
        entries.forEach((name, config) -> config.loaded(configManager));
    }

    @Override
    public void changed() {
        entries.forEach((name, config) -> config.changed());
    }

    @Override
    public void preSave(ConfigManager configManager) {
        entries.forEach((name, config) -> config.preSave(configManager));
    }

    @Override
    public void postSave(ConfigManager configManager) {
        entries.forEach((name, config) -> config.postSave(configManager));
    }
}