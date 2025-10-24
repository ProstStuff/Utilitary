package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.utility.config.ConfigCodec;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigMap<K, V> extends ConfigValue<Map<K, V>> {
    public ConfigCodec<Map<K, V>> codec;

    public ConfigMap(String name, Map<K, V> map, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec) {
        super(name, map);
        this.codec = ConfigCodec.map(keyCodec, valueCodec);
    }

    public ConfigMap(String name, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec) {
        this(name, new LinkedHashMap<>(), keyCodec, valueCodec);
    }

    public V put(K key, V value) {
        return this.value.put(key, value);
    }

    public V remove(K key) {
        return this.value.remove(key);
    }

    @Override
    public ConfigCodec<Map<K, V>> getCodec() {
        return codec;
    }
}