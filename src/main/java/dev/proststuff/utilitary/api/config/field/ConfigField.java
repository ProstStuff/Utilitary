package dev.proststuff.utilitary.api.config.field;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.api.config.ConfigFile;
import dev.proststuff.utilitary.api.config.codec.ConfigCodec;
import dev.proststuff.utilitary.api.config.impl.ConfigFileChild;

public abstract class ConfigField<V> implements ConfigFileChild {
    protected final String name;
    protected final V defaultValue;
    protected final ConfigCodec<V> codec;

    protected ConfigFile configFile;
    protected V value;
    protected V lastSavedValue = null;

    public ConfigField(String name, V value, ConfigCodec<V> codec) {
        this.name = name;
        this.defaultValue = value;
        this.codec = codec;
        this.value = value;
    }

    public V getCleanValue() {
        return lastSavedValue != null ? lastSavedValue : value != null ? value : defaultValue;
    }

    public boolean isDirty() {
        return lastSavedValue != null && lastSavedValue != value;
    }

    public String getAsString() {
        return get().toString();
    }

    public V get() {
        return value;
    }

    public final V getRaw() {
        return value;
    }

    public void setDefault() {
        this.value = defaultValue;
    }

    public final void set(V newValue) {
        if (this.lastSavedValue == null) {
            this.lastSavedValue = this.value;
        }
        this.value = validate(newValue);

        if (this.value == null) {
            setDefault();
        }
    }

    public V validate(V value) {
        return value;
    }

    @Override
    public final String getName() {
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
    public final JsonElement serialize() {
        lastSavedValue = null;
        return codec.encode(get());
    }

    @Override
    public final void deserialize(JsonElement jsonElement) {
        set(codec.decode(jsonElement));
    }
}