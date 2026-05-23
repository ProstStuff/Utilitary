package dev.proststuff.utilitary.api.config.codec;

import com.google.gson.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ConfigCodec<V>(Encoder<V> encoder, Decoder<V> decoder) {
    public JsonElement encode(V value, JsonSerializationContext context) {
        return encoder.encode(value, context);
    }

    public V decode(JsonElement json, JsonDeserializationContext context) {
        return decoder.decode(json, context);
    }

    public interface Encoder<V> {
        JsonElement encode(@NonNull V value, JsonSerializationContext context);
    }

    public interface Decoder<V> {
        @Nullable V decode(JsonElement jsonElement, JsonDeserializationContext context);
    }


    public static <V> ConfigCodec<List<V>> listOf(ConfigCodec<V> codec) {
        return new ConfigCodec<>(
                (list, context) -> {
                    JsonArray array = new JsonArray();
                    for (V v : list) {
                        array.add(codec.encode(v, context));
                    }
                    return array;
                },

                (jsonElement, context) -> {
                    if (jsonElement.isJsonArray()) {
                        JsonArray array = jsonElement.getAsJsonArray();
                        List<V> list = new ArrayList<>();

                        for (JsonElement element : array) {
                            codec.decode(element, context);
                        }

                        return list;
                    }

                    return null;
                }
        );
    }

    public static <V> ConfigCodec<Map<String, V>> mapOf(ConfigCodec<V> valueCodec, Map<String, V> map) {
        return new ConfigCodec<>(
                (_, context) -> {
                    JsonObject object = new JsonObject();

                    for (Map.Entry<String, V> entry : map.entrySet()) {
                        object.add(entry.getKey(), valueCodec.encode(entry.getValue(), context));
                    }

                    return object;
                },
                (jsonElement, context) -> {
                    if (jsonElement.isJsonObject()) {
                        JsonObject object = jsonElement.getAsJsonObject();

                        for (Map.Entry<String, JsonElement> entry : object.asMap().entrySet()) {
                            if (map.containsKey(entry.getKey())) {
                                map.put(entry.getKey(), valueCodec.decode(entry.getValue(), context));
                            }
                        }
                    }

                    return map;
                }
        );
    }
}