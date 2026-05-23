package dev.proststuff.utilitary.api.config.field.value;

import com.google.common.collect.ImmutableList;
import dev.proststuff.utilitary.api.config.codec.ConfigCodec;
import dev.proststuff.utilitary.api.config.field.ConfigField;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapConfigField<V> extends ConfigField<Map<String, V>> {
    public final ImmutableList<Map.Entry<String, V>> defaultMapEntries;

    public MapConfigField(String name, Map<String, V> map, ConfigCodec<V> valueCodec) {
        super(name, map, ConfigCodec.mapOf(valueCodec, map));
        this.defaultMapEntries = ImmutableList.copyOf(map.entrySet());
    }

    public MapConfigField(String name, ConfigCodec<V> valueCodec) {
        this(name, new LinkedHashMap<>(), valueCodec);
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
