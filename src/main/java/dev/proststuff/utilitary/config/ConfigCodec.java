package dev.proststuff.utilitary.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

/**
 * Lightweight config value serialization
 * @see dev.proststuff.utilitary.config.ConfigValue
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

    // Base types
    public static final ConfigCodec<String> STRING = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsString);
    public static final ConfigCodec<Boolean> BOOLEAN = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsBoolean);
    public static final ConfigCodec<Integer> INT = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsInt);
    public static final ConfigCodec<Float> FLOAT = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsFloat);
    public static final ConfigCodec<Double> DOUBLE = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsDouble);
    public static final ConfigCodec<Long> LONG = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsLong);

    // A much more advanced type
    public static final ConfigCodec<Identifier> IDENTIFIER = ConfigCodec.of(
            i -> new JsonPrimitive(i.toString()),
            jsonElement -> Identifier.tryParse(jsonElement.getAsString())
    );
    public static final ConfigCodec<Color> COLOR = ConfigCodec.of(
            c -> new JsonPrimitive(String.format("#%06X", c.getRGB() & 0xFFFFFF)),
            e -> Color.decode(e.getAsString())
    );
    public static final ConfigCodec<UUID> UUID = ConfigCodec.of(
            uuid -> new JsonPrimitive(uuid.toString()),
            jsonElement -> java.util.UUID.fromString(jsonElement.getAsString())
    );
    public static ConfigCodec<Long> TIME = ConfigCodec.of(
            ms -> {
                long h = ms / 3600000;
                long m = (ms % 360000) / 60000;
                long s = (ms % 60000) / 1000;

                StringBuilder builder = new StringBuilder();
                if (h > 0) builder.append(h).append("h ");
                if (m > 0) builder.append(m).append("m ");
                if (s > 0 || builder.isEmpty()) builder.append(s).append("s");
                return new JsonPrimitive(builder.toString().trim());
            },
            json -> {
                String str = json.getAsString().trim();
                long total = 0L;

                for (String part: str.split("\\s+")) {
                    if (part.endsWith("h")) total += Long.parseLong(part.replaceFirst("h", "")) * 3600000L;
                    else if (part.endsWith("m")) total += Long.parseLong(part.replaceFirst("m", "")) * 60000L;
                    else if (part.endsWith("s")) total += Long.parseLong(part.replaceFirst("s", "")) * 1000L;
                    else if (part.endsWith("ms")) total += Long.parseLong(part.replaceFirst("ms", ""));
                }

                return total;
            }
    );
    public static ConfigCodec<Vec3d> VECTOR3 = ConfigCodec.of(
            vec3d -> {
                JsonArray arr = new JsonArray(3);
                arr.add(vec3d.x);
                arr.add(vec3d.y);
                arr.add(vec3d.z);
                return arr;
            },
            json -> {
                JsonArray arr = json.getAsJsonArray();
                return new Vec3d(arr.get(0).getAsDouble(), arr.get(1).getAsDouble(), arr.get(2).getAsDouble());
            }
    );
    public static ConfigCodec<Vec2f> VECTOR2 = ConfigCodec.of(
            vec -> {
                JsonArray arr = new JsonArray(3);
                arr.add(vec.x);
                arr.add(vec.y);
                return arr;
            },
            json -> {
                JsonArray arr = json.getAsJsonArray();
                return new Vec2f(arr.get(0).getAsFloat(), arr.get(1).getAsFloat());
            }
    );
}
