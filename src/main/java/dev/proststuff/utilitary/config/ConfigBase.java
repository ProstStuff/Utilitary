package dev.proststuff.utilitary.config;

import com.google.gson.JsonElement;

public abstract class ConfigBase<T> {
    protected final String name;
    protected ConfigManager manager;

    public ConfigBase(String name) {
        this.name = name;
    }

    public String getName() {return name;}
    public ConfigManager getConfigManager() {return manager;}

    public abstract T get();
    public abstract T getDefault();

    public boolean set(T newValue) {return false;}
    public void setDefault() {set(getDefault());}
    public void setConfigManager(ConfigManager configManager) {this.manager = configManager;}

    abstract public JsonElement encode();
    abstract public void decode(JsonElement element);
}