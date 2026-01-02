package dev.proststuff.utilitary.config.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.proststuff.utilitary.config.value.ConfigValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Lightweight config value serialization
 * @see ConfigValue
 * @param encoder
 * @param decoder
 * @param <T>
 */
public record ConfigCodec<T>(Function<T, JsonElement> encoder, Function<JsonElement, T> decoder) {
    public JsonElement encode(T value) {
        return encoder.apply(value);
    }

    public T decode(JsonElement json) {
        try {
            return decoder.apply(json);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> ConfigCodec<T> of(Function<T, JsonElement> encoder, Function<JsonElement, T> decoder) {
        return new ConfigCodec<>(encoder, decoder);
    }

    public static <E extends Enum<E>> ConfigCodec<E> enums(Class<E> enumClass) {
        return ConfigCodec.of(
                e -> new JsonPrimitive(e.name()),
                json -> {
                    String name = json.getAsString();
                    try {
                        return Enum.valueOf(enumClass, name.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return enumClass.getEnumConstants()[0];
                    }
                }
        );
    }

    public static <K, V> ConfigCodec<Map<K, V>> map(ConfigCodec<K> keyCodec, ConfigCodec<V> valueCodec) {
        return ConfigCodec.of(
                m -> {
                    JsonObject object = new JsonObject();
                    m.forEach((k, v) -> object.add(keyCodec.encode(k).getAsString(), valueCodec.encode(v)));

                    return object;
                },
                jsonElement -> {
                    if (!jsonElement.isJsonObject()) return null;
                    Map<K, V> map = new LinkedHashMap<>();

                    for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                        K key = keyCodec.decode(new JsonPrimitive(entry.getKey()));
                        V val = valueCodec.decode(entry.getValue());
                        map.put(key, val);
                    }

                    return map;
                }
        );
    }

    public ConfigCodec<List<T>> asList() {
        return ConfigCodec.of(
                l -> {
                    JsonArray array = new JsonArray();

                    for (var t : l) {
                        array.add(encode(t));
                    }

                    return array;
                },
                jsonElement -> {
                    if (!jsonElement.isJsonArray()) return null;
                    JsonArray jsonElements = jsonElement.getAsJsonArray();
                    List<T> l = new ArrayList<>(jsonElements.size());

                    for (JsonElement element : jsonElement.getAsJsonArray()) {
                        l.add(decode(element));
                    }

                    return l;
                }
        );
    }

    public <K> ConfigCodec<Map<K, T>> asValueMap(ConfigCodec<K> keyCodec) {
        return ConfigCodec.of(
                m -> {
                    JsonObject object = new JsonObject();
                    m.forEach((k, v) -> object.add(keyCodec.encode(k).getAsString(), encode(v)));

                    return object;
                },
                jsonElement -> {
                    if (!jsonElement.isJsonObject()) return null;
                    Map<K, T> map = new LinkedHashMap<>();

                    for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                        K key = keyCodec.decode(new JsonPrimitive(entry.getKey()));
                        T val = decode(entry.getValue());
                        map.put(key, val);
                    }

                    return map;
                }
        );
    }

    public <V> ConfigCodec<Map<T, V>> asKeyMap(ConfigCodec<V> valueCodec) {
        return ConfigCodec.of(
                m -> {
                    JsonObject object = new JsonObject();
                    m.forEach((k, v) -> object.add(encode(k).getAsString(), valueCodec.encode(v)));

                    return object;
                },
                jsonElement -> {
                    if (!jsonElement.isJsonObject()) return null;
                    Map<T, V> map = new LinkedHashMap<>();

                    for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                        T key = decode(new JsonPrimitive(entry.getKey()));
                        V val = valueCodec.decode(entry.getValue());
                        map.put(key, val);
                    }

                    return map;
                }
        );
    }
}
