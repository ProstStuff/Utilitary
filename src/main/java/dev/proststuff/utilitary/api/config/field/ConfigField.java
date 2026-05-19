package dev.proststuff.utilitary.api.config.field;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import dev.proststuff.utilitary.api.config.codec.ConfigCodec;
import dev.proststuff.utilitary.api.config.impl.ConfigSerializable;

public abstract class ConfigField<V> implements ConfigSerializable {
    protected final String name;
    protected final V defaultValue;
    protected final ConfigCodec<V> codec;

    protected V value;
    public ConfigField(String name, V value, ConfigCodec<V> codec) {
        this.name = name;
        this.defaultValue = value;
        this.codec = codec;
        this.value = value;
    }

    public final String getName() {
        return name;
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
        this.value = validate(newValue);

        if (this.value == null) {
            setDefault();
        }
    }

    public V validate(V value) {
        return value;
    }

    @Override
    public final JsonElement serialize(JsonSerializationContext context) {
        return codec.encode(get(), context);
    }

    @Override
    public final void deserialize(JsonElement jsonElement, JsonDeserializationContext context) {
        set(codec.decode(jsonElement, context));
    }
}
