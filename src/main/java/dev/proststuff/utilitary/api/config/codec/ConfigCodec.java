package dev.proststuff.utilitary.api.config.codec;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ConfigCodec<V>(Encoder<V> encoder, Decoder<V> decoder) {
    public JsonElement encode(V value) {
        return encoder.encode(value);
    }

    public V decode(JsonElement json) {
        return decoder.decode(json);
    }

    public interface Encoder<V> {
        JsonElement encode(@NonNull V value);
    }

    public interface Decoder<V> {
        @Nullable V decode(JsonElement jsonElement);
    }

    public static <V> ConfigCodec<List<V>> listOf(ConfigCodec<V> codec, boolean immutable) {
        return new ConfigCodec<>(
                (list) -> {
                    JsonArray array = new JsonArray();
                    for (V v : list) {
                        array.add(codec.encode(v));
                    }
                    return array;
                },

                (jsonElement) -> {
                    if (jsonElement.isJsonArray()) {
                        JsonArray array = jsonElement.getAsJsonArray();
                        List<V> list = new ArrayList<>();

                        for (JsonElement element : array) {
                            list.add(codec.decode(element));
                        }

                        return immutable ? ImmutableList.copyOf(list) : list;
                    }

                    return null;
                }
        );
    }

    public static <V> ConfigCodec<Map<String, V>> mapOf(ConfigCodec<V> valueCodec, Map<String, V> map) {
        return new ConfigCodec<>(
                (_) -> {
                    JsonObject object = new JsonObject();

                    for (Map.Entry<String, V> entry : map.entrySet()) {
                        object.add(entry.getKey(), valueCodec.encode(entry.getValue()));
                    }

                    return object;
                },
                (jsonElement) -> {
                    if (jsonElement.isJsonObject()) {
                        map.clear();
                        JsonObject object = jsonElement.getAsJsonObject();

                        for (Map.Entry<String, JsonElement> entry : object.asMap().entrySet()) {
                            V decoded = valueCodec.decode(entry.getValue());

                            if (decoded != null) {
                                map.put(entry.getKey(), decoded);

                            }
                        }
                    }

                    return map;
                }
        );
    }
}