package dev.proststuff.utilitary.serialization.content.field;

import com.google.common.collect.ImmutableList;
import dev.proststuff.utilitary.serialization.codec.ConfigCodec;
import dev.proststuff.utilitary.serialization.content.ConfigField;

import java.util.Map;

public class MapConfigField<V> extends ConfigField<Map<String, V>> {
    public final ImmutableList<Map.Entry<String, V>> defaultMapEntries;

    public MapConfigField(String name, Map<String, V> value, ConfigCodec<V> valueCodec) {
        super(name, value, ConfigCodec.mapOf(valueCodec, value));
        this.value = value;
        this.defaultMapEntries = ImmutableList.copyOf(value.entrySet());
    }

    public V get(String key) {
        return get().get(key);
    }

    public V put(String key, V value) {
        return get().put(key, value);
    }

    public V remove(String key) {
        return get().remove(key);
    }

    public void clear() {
        get().clear();
    }

    @Override
    public void setDefault() {
        Map<String, V> map = get();
        clear();

        for (Map.Entry<String, V> mapEntry : defaultMapEntries) {
            map.put(mapEntry.getKey(), mapEntry.getValue());
        }
    }
}
