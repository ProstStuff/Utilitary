package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

import java.util.function.Consumer;

public class ConfigValue<T> implements ICanConfigure<T> {
    protected @Expose T value;
    protected final T default_value;
    protected T previous_value;
    protected final ConfigCodec<T> codec;

    private final String name;
    private final boolean runtimeOnly;
    private boolean loaded = false;

    private Runnable changedEvent;
    private Consumer<ConfigManager> loadedOnceEvent;
    private Consumer<ConfigManager> serializingEvent;
    private Consumer<ConfigManager> serializedEvent;
    private Consumer<ConfigManager> deserializingEvent;
    private Consumer<ConfigManager> deserializedEvent;

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
        T prev = value;
        this.previous_value = prev;
        this.value = newValue;

        changed();
        return prev;
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        try {
            return codec.encode(value);
        } catch (Exception e) {
            manager.warn(IFancyLogging.LogType.WARN, "Unable to serialize config value {}, serializing default: {}", name, e.getMessage());
            return codec.encode(default_value);
        }
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            value = codec.decode(element);
        } catch (Exception e) {
            manager.warn(IFancyLogging.LogType.WARN, "Unable to deserialize config value {}, using defaults: {}", name, e.getMessage());
        }
    }

    public void setLoaded(ConfigManager manager) {
        if (this.loaded) return;
        this.loaded = true;

        if (this.loadedOnceEvent != null) {
            this.loadedOnceEvent.accept(manager);
        }
    }

    public T setDefault() {return set(default_value);}
    public T undo() {return set(previous_value);}

    public String getName() {return name;}
    public T get() {return value;}
    public T getDefault() {return default_value;}
    public T getPrevious() {return previous_value;}
    public boolean wasLoaded() {return loaded;}
    public boolean isRuntimeOnly() {return this.runtimeOnly;}

    public void changed(Runnable event) {this.changedEvent = event;}
    public void loadedOnce(Consumer<ConfigManager> event) {this.loadedOnceEvent = event;}
    public void serializing(Consumer<ConfigManager> event) {this.serializingEvent = event;}
    public void serialized(Consumer<ConfigManager> event) {this.serializedEvent = event;}
    public void deserializing(Consumer<ConfigManager> event) {this.deserializingEvent = event;}
    public void deserialized(Consumer<ConfigManager> event) {this.deserializedEvent = event;}

    public void changed() {if (this.changedEvent != null) this.changedEvent.run();}
    @Override
    public void serializing(ConfigManager configManager) {if (this.serializingEvent != null) this.serializingEvent.accept(configManager);}
    @Override
    public void serialized(ConfigManager configManager) {if (this.serializedEvent != null) this.serializedEvent.accept(configManager);}
    @Override
    public void deserializing(ConfigManager configManager) {if (this.deserializingEvent != null) this.deserializingEvent.accept(configManager);}
    @Override
    public void deserialized(ConfigManager configManager) {
        if (this.deserializedEvent != null) this.deserializedEvent.accept(configManager);
        changed();
        setLoaded(configManager);
    }
}