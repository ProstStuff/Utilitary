package dev.proststuff.utilitary.config.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.UUID;

public class ConfigCodecs {
    public static final ConfigCodec<String> STRING = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsString);
    public static final ConfigCodec<Boolean> BOOLEAN = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsBoolean);
    public static final ConfigCodec<Integer> INT = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsInt);
    public static final ConfigCodec<Float> FLOAT = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsFloat);
    public static final ConfigCodec<Double> DOUBLE = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsDouble);
    public static final ConfigCodec<Long> LONG = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsLong);

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
