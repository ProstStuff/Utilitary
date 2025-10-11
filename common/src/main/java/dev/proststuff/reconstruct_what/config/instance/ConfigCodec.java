package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public record ConfigCodec<T>(Function<T, JsonElement> serializer, Function<JsonElement, T> deserializer) {
    public static <T> ConfigCodec<T> of(Function<T, JsonElement> ser, Function<JsonElement, T> de) {
        return new ConfigCodec<>(ser, de);
    }

    public static <E extends Enum<E>> ConfigCodec<E> enums(Class<E> enumClass) {
        return ConfigCodec.of(
                e -> new JsonPrimitive(e.name()),
                json -> {
                    String name = json.getAsString();
                    try {
                        return Enum.valueOf(enumClass, name.toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException ex) {
                        return enumClass.getEnumConstants()[0];
                    }
                }
        );
    }

    public static <T> ConfigCodec<List<T>> list(List<T> list) {
        return ConfigCodec.of(
                e -> {
                    throw new UnsupportedOperationException("List can't have their own codec, only elements can. This codec shouldn't be used.");
                },
                json -> {
                    throw new UnsupportedOperationException("List can't have their own codec, only elements can. This codec shouldn't be used.");
                }
        );
    }

    public static <K, V> ConfigCodec<Map<K, V>> map(Map<K, V> map) {
        return ConfigCodec.of(
                e -> {
                    throw new UnsupportedOperationException("Map can't have their own codec, only key and value can. This codec shouldn't be used.");
                },
                json -> {
                    throw new UnsupportedOperationException("Map can't have their own codec, only key and value can. This codec shouldn't be used.");
                }
        );
    }

    public JsonElement encode(T value) {
        return serializer.apply(value);
    }

    public T decode(JsonElement json) {
        return deserializer.apply(json);
    }
}