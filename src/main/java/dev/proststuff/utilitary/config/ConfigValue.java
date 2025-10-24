package dev.proststuff.utilitary.config;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.utility.IFancyLogging;
import dev.proststuff.utilitary.utility.config.ConfigCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ConfigValue<T> extends ConfigBase<T> {
    protected boolean runtimeOnly;
    protected final List<Consumer<ConfigValue<T>>> changedListeners;
    protected final T defaultValue;
    protected T oldValue;
    protected T value;

    protected ConfigValue(String name, T value) {
        super(name);
        this.defaultValue = value;
        this.value = value;
        this.oldValue = null;

        this.runtimeOnly = false;
        this.changedListeners = new ArrayList<>();
    }

    public ConfigValue<T> runtimeOnly() {
        this.runtimeOnly = true;
        return this;
    }

    public ConfigValue<T> listen(Consumer<ConfigValue<T>> listener) {
        changedListeners.add(listener);
        listener.accept(this);
        return this;
    }

    public abstract ConfigCodec<T> getCodec();

    public void changed() {
        getConfigManager().info(IFancyLogging.LogType.ACTION, "ConfigValue changed, {} changed to {}", oldValue, value);
        for (Consumer<ConfigValue<T>> changedListener : changedListeners) {
            changedListener.accept(this);
        }
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }

    public T getOld() {
        return this.oldValue;
    }

    @Override
    public boolean set(T value) {
        if (this.value.equals(value)) return false;
        this.oldValue = this.value;
        this.value = value;

        changed();
        return true;
    }

    public boolean isRuntimeOnly() {
        return runtimeOnly;
    }

    @Override
    public JsonElement encode() {
        return getCodec().encode(get());
    }

    @Override
    public void decode(JsonElement element) {
        T v = getCodec().decode(element);

        if (v != null) {
            this.value = v;
        } else {
            setDefault();
        }
    }
}