package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigMap<K, V> extends ConfigValue<Map<K, V>> {
    public ConfigCodec<Map<K, V>> codec;

    public ConfigMap(Identifier identifier, Map<K, V> map, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec) {
        super(identifier, map);
        this.codec = ConfigCodec.map(keyCodec, valueCodec);
    }

    public ConfigMap(Identifier identifier, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec) {
        this(identifier, new LinkedHashMap<>(), keyCodec, valueCodec);
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