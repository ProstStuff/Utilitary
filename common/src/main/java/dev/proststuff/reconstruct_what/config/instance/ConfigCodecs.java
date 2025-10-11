package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.UUID;

public class ConfigCodecs {
    public static final ConfigCodec<Boolean> BOOL = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsBoolean);
    public static final ConfigCodec<Integer> INT = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsInt);
    public static final ConfigCodec<Long> LONG = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsLong);
    public static final ConfigCodec<Double> DOUBLE = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsDouble);
    public static final ConfigCodec<String> STRING = ConfigCodec.of(JsonPrimitive::new, JsonElement::getAsString);
    public static final ConfigCodec<UUID> UUID = ConfigCodec.of(u -> new JsonPrimitive(u.toString()), e -> java.util.UUID.fromString(e.getAsString()));
    public static final ConfigCodec<ResourceLocation> RESOURCE_LOCATION = ConfigCodec.of(
            rl -> new JsonPrimitive(rl.toString()), e -> ResourceLocation.tryParse(e.getAsString()));
    public static final ConfigCodec<Color> COLOR = ConfigCodec.of(
            c -> new JsonPrimitive(String.format("#%06X", c.getRGB() & 0xFFFFFF)),
            e -> Color.decode(e.getAsString())
    );
    public static ConfigCodec<Vec2> VEC2 = ConfigCodec.of(
            e -> {
                JsonArray v = new JsonArray(2);

                v.add(new JsonPrimitive(e.x));
                v.add(new JsonPrimitive(e.y));
                return v;
            },
            json -> new Vec2(json.getAsJsonArray().get(0).getAsFloat(), json.getAsJsonArray().get(1).getAsFloat())
    );

    public static ConfigCodec<Vec3> VEC3 = ConfigCodec.of(
            e -> {
                JsonArray v = new JsonArray(3);

                v.add(new JsonPrimitive(e.x));
                v.add(new JsonPrimitive(e.y));
                v.add(new JsonPrimitive(e.z));
                return v;
            },
            json -> new Vec3(json.getAsJsonArray().get(0).getAsFloat(), json.getAsJsonArray().get(1).getAsFloat(), json.getAsJsonArray().get(2).getAsFloat())
    );
}