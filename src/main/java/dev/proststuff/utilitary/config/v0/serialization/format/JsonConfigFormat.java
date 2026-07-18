package dev.proststuff.utilitary.config.v0.serialization.format;

import com.google.gson.*;
import dev.proststuff.utilitary.config.v0.ConfigType;
import dev.proststuff.utilitary.config.v0.serialization.metadata.ConfigMetadata;
import dev.proststuff.utilitary.api.v0.utility.SimpleIdentifier;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigFormat implements ConfigFormat<JsonConfigFormat.FormatSettings> {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .serializeNulls()
            .setStrictness(Strictness.LENIENT)
            .enableComplexMapKeySerialization()
            .create();

    public static final JsonConfigFormat INSTANCE = new JsonConfigFormat();

    private JsonConfigFormat() {}

    @Override
    public @NonNull JsonElement read(Path path, FormatSettings formatSettings) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return formatSettings.gson.fromJson(reader, JsonElement.class);
        }
    }

    @Override
    public void write(Path path, JsonElement element, FormatSettings formatSettings) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            formatSettings.gson.toJson(element, writer);
        }
    }

    @Override
    public <C, M extends ConfigMetadata> @NonNull FormatSettings create(SimpleIdentifier fileName, ConfigType<C, M, FormatSettings> type) {
        return new FormatSettings();
    }

    @Override
    public @NonNull String suffix() {
        return "json";
    }

    public record FormatSettings(Gson gson) {
        public FormatSettings() {
            this(GSON);
        }
    }
}