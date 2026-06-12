package dev.proststuff.utilitary.api.config.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import dev.proststuff.utilitary.api.utility.Color;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

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

    public static final ConfigCodec<Vec3> VEC3 = new ConfigCodec<>(
            (value) -> {
                JsonArray array = new JsonArray(3);
                array.add(value.x);
                array.add(value.y);
                array.add(value.z);
                return array;
            },
            (jsonElement) -> {
                if (jsonElement.isJsonArray()) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new Vec3(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
                }

                return null;
            }
    );

    public static final ConfigCodec<Vec2> VEC2 = new ConfigCodec<>(
            (value) -> {
                JsonArray array = new JsonArray(2);
                array.add(value.x);
                array.add(value.y);
                return array;
            },
            (jsonElement) -> {
                if (jsonElement.isJsonArray()) {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new Vec2(array.get(0).getAsFloat(), array.get(1).getAsFloat());
                }

                return null;
            }
    );
}
