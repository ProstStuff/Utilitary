package dev.proststuff.utilitary.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.proststuff.utilitary.utility.IFancyLogging;

import java.util.ArrayList;
import java.util.List;

public class ConfigOption extends ConfigBase<List<ConfigBase<?>>> {
    protected List<ConfigBase<?>> entries = new ArrayList<>();

    public ConfigOption(String name) {
        super(name);
    }

    public List<ConfigBase<?>> getEntries() {
        return entries;
    }
    
    @Override
    public void setConfigManager(ConfigManager configManager) {
        super.setConfigManager(configManager);
        
        for (ConfigBase<?> entry : entries) {
            entry.setConfigManager(configManager);
        }
    }

    @Override
    public List<ConfigBase<?>> get() {
        return entries;
    }

    @Override
    public List<ConfigBase<?>> getDefault() {
        return new ArrayList<>();
    }

    public ConfigOption add(ConfigBase<?> config) {
        if (config == this) throw new StackOverflowError("WHAT ARE YOU DOING? ARE YOU TRYING TO CREATE A SINGULARITY???");

        if (this.entries == null) this.entries = getDefault();

        for (ConfigBase<?> cfg : this.entries) {
            if (cfg.getName().equals(config.getName())) {
                throw new IllegalStateException("Duplicate config name, a config named " + cfg.getName() + " is already present.");
            }
        }

        config.setConfigManager(manager);
        this.entries.add(config);
        return this;
    }

    @Override
    public JsonElement encode() {
        JsonObject jsonObject = new JsonObject();

        for (ConfigBase<?> entry : entries) {
            getConfigManager().info(IFancyLogging.LogType.SUB, "Encoding {}", entry.name);
            if (entry instanceof ConfigValue<?> value && !value.isRuntimeOnly()) {
                jsonObject.add(value.getName(), value.encode());
            } else {
                jsonObject.add(entry.getName(), entry.encode());
            }
        }

        return jsonObject;
    }

    @Override
    public void decode(JsonElement element) {
        if (!element.isJsonObject()) {
            getConfigManager().warn("{} ConfigOption receive a different type of JsonElement; {}", name, element.toString());
            setDefault();
            return;
        }
        JsonObject json = element.getAsJsonObject();

        for (ConfigBase<?> entry : entries) {
            getConfigManager().info(IFancyLogging.LogType.SUB, "Decoding {}", entry.name);
            String name = entry.getName();
            JsonElement child = json.get(name);
            entry.decode(child);
        }

    }
}
