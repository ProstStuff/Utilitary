package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

public class ConfigValue<T> implements ICanConfigure<T> {
    protected @Expose T value;
    protected final T default_value;
    protected T previous_value;
    protected final ConfigCodec<T> codec;

    private final String name;
    private final boolean runtimeOnly;
    private boolean loaded = false;

    public ConfigValue(String name, T defaultValue, ConfigCodec<T> codec, boolean runtimeOnly) {
        this.name = name;
        this.default_value = defaultValue;
        this.value = defaultValue;
        this.previous_value = defaultValue;
        this.codec = codec;
        this.runtimeOnly = runtimeOnly;
    }

    public ConfigValue(String name, T defaultValue, ConfigCodec<T> codec) {
        this(name, defaultValue, codec, false);
    }

    public T set(T newValue) {
        T prev = this.get();
        this.previous_value = prev;
        this.value = newValue;
        return prev;
    }

    public T setDefault() {
        return set(this.default_value);
    }

    public T undo() {
        return set(this.previous_value);
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        try {
            return codec.encode(value);
        } catch (Exception e) {
            manager.warn(IFancyLogging.LogType.WARN, "Serialization failed for {}: {}", name, e.getMessage());
            return codec.encode(default_value);
        }
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            value = codec.decode(element);
            setLoaded();
        } catch (Exception e) {
            manager.warn(IFancyLogging.LogType.WARN, "Deserialization failed for {}: {}", name, e.getMessage());
        }
    }

    protected void setLoaded() {
        this.loaded = true;
    }

    public String getName() { return name; }
    public T get() { return value; }
    public T getDefault() { return this.default_value; }
    public T getPrevious() { return previous_value; }
    public boolean wasLoaded() { return loaded; }
    public boolean isRuntimeOnly() { return this.runtimeOnly; }
}