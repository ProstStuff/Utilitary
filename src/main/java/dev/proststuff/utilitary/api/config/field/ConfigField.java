package dev.proststuff.utilitary.api.config.field;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import dev.proststuff.utilitary.api.config.ConfigFile;
import dev.proststuff.utilitary.api.config.codec.ConfigCodec;
import dev.proststuff.utilitary.api.config.impl.ConfigFileChild;
import org.jspecify.annotations.Nullable;

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

    public @Nullable V getCleanValue() {
        return lastSavedValue;
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
    public final JsonElement serialize(JsonSerializationContext context) {
        lastSavedValue = null;
        return codec.encode(get(), context);
    }

    @Override
    public final void deserialize(JsonElement jsonElement, JsonDeserializationContext context) {
        set(codec.decode(jsonElement, context));
    }
}