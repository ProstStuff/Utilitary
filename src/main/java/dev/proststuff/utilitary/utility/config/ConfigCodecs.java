package dev.proststuff.utilitary.utility.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.UUID;

/**
 * Premade config codecs
 */
public final class ConfigCodecs {
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
}