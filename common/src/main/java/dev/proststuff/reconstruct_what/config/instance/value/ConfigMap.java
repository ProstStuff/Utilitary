package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.ConfigCodec;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigMap<K, V> extends ConfigValue<Map<K, V>> {
    private final ConfigCodec<K> keyCodec;
    private final ConfigCodec<V> valueCodec;

    public ConfigMap(String name, Map<K, V> map, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec, boolean runtimeOnly) {
        super(name, map, ConfigCodec.map(map), runtimeOnly);
        this.keyCodec = keyCodec;
        this.valueCodec = valueCodec;
    }

    public ConfigMap(String name, Map<K, V> map, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec) {
        this(name, map, keyCodec, valueCodec, false);
    }

    public ConfigMap(String name, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec, boolean runtimeOnly) {
        this(name, new LinkedHashMap<>(), keyCodec, valueCodec, runtimeOnly);
    }

    public ConfigMap(String name, ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec) {
        this(name, keyCodec, valueCodec, false);
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        JsonObject json = new JsonObject();
        for (Map.Entry<K, V> entry : value.entrySet()) {
            JsonElement keyJson = keyCodec.encode(entry.getKey());
            json.add(keyJson.getAsString(), valueCodec.encode(entry.getValue()));
        }
        return json;
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        if (!element.isJsonObject()) return;
        JsonObject json = element.getAsJsonObject();

        Map<K, V> map = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            K key = keyCodec.decode(new JsonPrimitive(entry.getKey()));
            V val = valueCodec.decode(entry.getValue());
            map.put(key, val);
        }

        this.value = map;
    }

    public V put(K key, V value) {
        return this.value.put(key, value);
    }

    public V remove(K key) {
        return this.value.remove(key);
    }
}
