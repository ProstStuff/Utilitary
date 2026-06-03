package dev.proststuff.utilitary.api.config.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import dev.proststuff.utilitary.api.utility.Color;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class ConfigCodecs {
    public static final ConfigCodec<String> STRING = new ConfigCodec<>(
            JsonPrimitive::new,
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return jsonElement.getAsString();
                }

                return null;
            });

    public static final ConfigCodec<Boolean> BOOLEAN = new ConfigCodec<>(
            JsonPrimitive::new,
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return jsonElement.getAsBoolean();
                }

                return null;
            }
    );

    public static final ConfigCodec<Integer> INTEGER = new ConfigCodec<>(
            JsonPrimitive::new,
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return jsonElement.getAsInt();
                }

                return null;
            }
    );

    public static final ConfigCodec<Float> FLOAT = new ConfigCodec<>(
            JsonPrimitive::new,
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return jsonElement.getAsFloat();
                }

                return null;
            }
    );

    public static final ConfigCodec<Double> DOUBLE = new ConfigCodec<>(
            JsonPrimitive::new,
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return jsonElement.getAsDouble();
                }

                return null;
            }
    );

    public static final ConfigCodec<Long> LONG = new ConfigCodec<>(
            JsonPrimitive::new,
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return jsonElement.getAsLong();
                }

                return null;
            }
    );

    public static final ConfigCodec<Color> COLOR = new ConfigCodec<>(
            (value) -> new JsonPrimitive(value.toString()),
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    String hex = jsonElement.getAsString();

                    if (hex.startsWith("#")) {
                        hex = hex.substring(1);
                    }

                    if (hex.length() == 6) {
                        hex = "FF" + hex;
                    }

                    return new Color((int) Long.parseLong(hex, 16));
                }

                return null;
            }
    );

    public static final ConfigCodec<SimpleIdentifier> SIMPLE_IDENTIFIER = new ConfigCodec<>(
            (value) -> new JsonPrimitive(value.toString()),
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return SimpleIdentifier.tryParse(jsonElement.getAsString());
                }
                return null;
            }
    );

    public static final ConfigCodec<Identifier> IDENTIFIER = new ConfigCodec<>(
            (value) -> new JsonPrimitive(value.toString()),
            (jsonElement) -> {
                if (jsonElement.isJsonPrimitive()) {
                    return Identifier.parse(jsonElement.getAsString());
                }

                return null;
            }
    );

    public static final ConfigCodec<BlockPos> BLOCK_POS = new ConfigCodec<>(
            (value) -> {
                JsonArray array = new JsonArray();
                array.add(value.getX());
                array.add(value.getY());
                array.add(value.getZ());
                return array;
            },
            (jsonElement) -> {
                if (jsonElement.isJsonArray()) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new BlockPos(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
                }

                return null;
            }
    );

    public static final ConfigCodec<Vector3i> VECTOR3I = new ConfigCodec<>(
            (value) -> {
                JsonArray array = new JsonArray();
                array.add(value.x());
                array.add(value.y());
                array.add(value.z());
                return array;
            },
            (jsonElement) -> {
                if (jsonElement.isJsonArray()) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new Vector3i(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
                }

                return null;
            }
    );

    public static final ConfigCodec<Vector3f> VECTOR3F = new ConfigCodec<>(
            (value) -> {
                JsonArray array = new JsonArray();
                array.add(value.x());
                array.add(value.y());
                array.add(value.z());
                return array;
            },
            (jsonElement) -> {
                if (jsonElement.isJsonArray()) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
                }

                return null;
            }
    );

    public static final ConfigCodec<Vector3d> VECTOR3D = new ConfigCodec<>(
            (value) -> {
                JsonArray array = new JsonArray();
                array.add(value.x());
                array.add(value.y());
                array.add(value.z());
                return array;
            },
            (jsonElement) -> {
                if (jsonElement.isJsonArray()) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new Vector3d(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
                }

                return null;
            }
    );
}
