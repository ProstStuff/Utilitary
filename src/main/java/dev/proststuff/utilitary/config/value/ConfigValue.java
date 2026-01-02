package dev.proststuff.utilitary.config.value;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.config.ConfigBase;
import dev.proststuff.utilitary.config.utility.ConfigCodec;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class ConfigValue<T> extends ConfigBase {
    protected T value;
    protected final T defaultValue;
    protected boolean noSave = false;

    protected Function<T, T> changedCallback = (t) -> {
        if (Utilitary.CONFIG.debugEnabled()) {
            Utilitary.CONFIG.getLogger().info("{} value changed to {}", this, t);
        }

        return t;
    };

    public ConfigValue(Identifier identifier, T value) {
        super(identifier);
        this.value = value;
        this.defaultValue = value;
    }

    public abstract ConfigCodec<T> getCodec();

    /**
     * Disable saving and syncing for this config
     */
    public void noSave() {
        this.noSave = true;
    }

    public T get() {
        return value;
    }

    public T getDefault() {
        return defaultValue;
    }

    /**
     * @param value The new value
     * @return Whether the new value is set
     */
    public boolean set(T value) {
        if (!value.equals(this.value)) {
            T modified = changedCallback.apply(value);
            this.value = modified != null ? modified : this.defaultValue;

            return true;
        }

        return false;
    }

    /**
     * @return The string JSON to be written to the files
     */
    @Nullable
    @Override
    public JsonElement encode() {
        return noSave ? null : getCodec().encode(value);
    }

    /**
     * @param element To be decoded by {@link ConfigCodec}
     */
    @Override
    public void decode(JsonElement element) {
        T decoded = getCodec().decode(element);

        if (decoded != null && !this.value.equals(decoded)) {
            set(decoded);
        }
    }

    /**
     * @param callback The callback to be chained with the previous for when the value is changed. Return null to force reset the value.
     * @return The callback chain
     */
    public Function<T, T> onChange(Function<T, T> callback) {
        this.changedCallback = this.changedCallback.andThen(callback);
        return this.changedCallback;
    }

    @Override
    public String toString() {
        return "ConfigValue<" + defaultValue.getClass().getName() + ">$" + identifier + "=" + value;
    }
}