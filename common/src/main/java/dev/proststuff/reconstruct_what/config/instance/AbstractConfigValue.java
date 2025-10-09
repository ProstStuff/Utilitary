package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

import java.util.function.Consumer;

public abstract class AbstractConfigValue<T> implements ICanConfigure<T> {
    protected @Expose T value;
    protected final T defaultValue;
    protected T previousValue;

    private Consumer<AbstractConfigValue<T>> listener;
    private final String name;

    private final boolean runtimeOnly;

    public AbstractConfigValue(String name, T value, boolean runtimeOnly) {
        this.name = name;
        this.defaultValue = value;
        this.runtimeOnly = runtimeOnly;
        this.value = this.defaultValue;
        this.previousValue = this.defaultValue;
    }

    public AbstractConfigValue(String name, T value) {
        this(name, value, false);
    }

    public void setListener(Consumer<AbstractConfigValue<T>> listener) {
        this.listener = listener;
    }

    public boolean isRuntimeOnly() {
        return this.runtimeOnly;
    }

    public T get() {
        return this.value;
    }

    @Override
    public void onChange() {
        if (this.listener != null) {
            this.listener.accept(this);
        }
    }

    public T getDefault() {
        return this.defaultValue;
    }

    public void setDefault() {
        this.set(this.getDefault());
    }

    public T getPrevious() {
        return this.previousValue;
    }

    public T set(T value) {
        this.previousValue = this.value;
        this.value = value;
        return this.previousValue;
    }

    public T reset() {
        this.previousValue = this.value;
        this.value = this.getDefault();
        return this.previousValue;
    }

    public T previous() {
        T val1 = this.previousValue;
        T val2 = this.value;

        this.value = val1;
        this.previousValue = val2;

        return val1;
    }

    public boolean shouldSave() {
        return !runtimeOnly;
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        if (value instanceof Number num) return new JsonPrimitive(num);
        if (value instanceof Boolean bool) return new JsonPrimitive(bool);
        if (value instanceof String str) return new JsonPrimitive(str);
        if (value instanceof Character c) return new JsonPrimitive(c);

        manager.warn(IFancyLogging.LogType.WARN, "ConfigValue {} does not have a custom serialize methods, using `toString()`. Please override this function later.", this.getClass().getName());
        return new JsonPrimitive(this.value.toString());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("ConfigValue{name='%s', value=%s}", name, value);
    }
}