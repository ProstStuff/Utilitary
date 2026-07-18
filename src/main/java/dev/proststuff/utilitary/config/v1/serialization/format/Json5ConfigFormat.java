package dev.proststuff.utilitary.config.v1.serialization.format;

import com.google.gson.*;
import de.marhali.json5.*;
import de.marhali.json5.config.DuplicateKeyStrategy;
import dev.proststuff.utilitary.config.v1.ConfigConstants;
import dev.proststuff.utilitary.config.v1.ConfigType;
import dev.proststuff.utilitary.config.v1.serialization.Comment;
import dev.proststuff.utilitary.config.v1.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.api.v1.utility.SimpleIdentifier;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Json5ConfigFormat implements ConfigFormat<Json5ConfigFormat.FormatSettings> {
    public static final Json5 JSON5 = Json5.builder(builder -> builder
            .prettyPrinting()
            .writeComments()
            .parseComments()
            .trailingComma()
            .quoteless()
            .allowBinaryLiterals()
            .allowLongUnicodeEscapes()
            .duplicateKeyStrategy(DuplicateKeyStrategy.UNIQUE)
            .build()
    );

    public static final Json5ConfigFormat INSTANCE = new Json5ConfigFormat();

    private Json5ConfigFormat() {}

    @Override
    public @NonNull JsonElement read(Path path, FormatSettings formatSettings) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return fromJson5(formatSettings.json5().parse(reader));
        }
    }

    @Override
    public void write(Path path, JsonElement element, FormatSettings formatSettings) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            Json5Object object = toJson5(element).getAsJson5Object();
            object.add(ConfigConstants.METADATA_KEY, comments(object.remove(ConfigConstants.METADATA_KEY), formatSettings.metadataComments));
            object.add(ConfigConstants.DATA_KEY, comments(object.remove(ConfigConstants.DATA_KEY), formatSettings.dataComments));
            formatSettings.json5().serialize(object, writer);
        }
    }

    @Override
    public <C, M extends ConfigMetadata> @NonNull FormatSettings create(SimpleIdentifier fileName, ConfigType<C, M, FormatSettings> type) {
        return withComment(type);
    }

    @Override
    public @NonNull String suffix() {
        return "json5";
    }

    public static FormatSettings withComment(ConfigType<?, ?, ?> type) {
        return new FormatSettings(JSON5, type.metadata().comment(), type.comments());
    }

    public record FormatSettings(Json5 json5, Comment metadataComments, Comment dataComments) {}

    public static <E extends Json5Element> E comments(E element, Comment comment) {
        if (element.isJson5Object()) {
            for (Map.Entry<String, Json5Element> entry : element.getAsJson5Object().entrySet()) {
                comments(entry.getValue(), comment.get(entry.getKey()));
            }
        }

        element.setComment(comment.getNonEmptyString());
        return element;
    }

    public static @NonNull Json5Element toJson5(JsonElement json) {
        switch (json) {
            case null -> new Json5Null();
            case JsonNull _ -> new Json5Null();
            case JsonObject object -> {
                Json5Object json5 = new Json5Object();

                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    String key = entry.getKey();
                    json5.add(key, toJson5(entry.getValue()));
                }

                return json5;
            }
            case JsonArray array -> {
                Json5Array json5 = new Json5Array();

                for (JsonElement element : array) {
                    json5.add(toJson5(element));
                }

                return json5;
            }
            case JsonPrimitive primitive -> {
                if (primitive.isBoolean()) {
                    return Json5Primitive.fromBoolean(primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    return Json5Primitive.fromNumber(primitive.getAsNumber());
                } else if (primitive.isString()) {
                    return Json5Primitive.fromString(primitive.getAsString());
                }
            }

            default -> {}
        }

        throw new IllegalStateException("Unsupported JsonElement: " + json);
    }

    public static @NonNull JsonElement fromJson5(Json5Element json5) {
        switch (json5) {
            case null -> {
                return JsonNull.INSTANCE;
            }
            case Json5Null _ -> {
                return JsonNull.INSTANCE;
            }
            case Json5Object object -> {
                JsonObject json = new JsonObject();

                for (Map.Entry<String, Json5Element> entry : object.entrySet()) {
                    json.add(entry.getKey(), fromJson5(entry.getValue()));
                }

                return json;
            }
            case Json5Array array -> {
                JsonArray json = new JsonArray();

                for (Json5Element element : array) {
                    json.add(fromJson5(element));
                }

                return json;
            }
            case Json5Primitive primitive -> {
                if (primitive.isBoolean()) return new JsonPrimitive(primitive.getAsBoolean());
                if (primitive.isNumber()) return new JsonPrimitive(primitive.getAsNumber());
                if (primitive.isString()) return new JsonPrimitive(primitive.getAsString());
            }
            default -> {}
        }

        throw new IllegalArgumentException("Unsupported Json5Element: " + json5);
    }
}
