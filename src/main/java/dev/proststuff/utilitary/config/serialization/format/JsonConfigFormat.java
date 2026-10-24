package dev.proststuff.utilitary.config.serialization.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.Strictness;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import dev.proststuff.utilitary.config.ConfigConstants;
import dev.proststuff.utilitary.config.ConfigType;
import dev.proststuff.utilitary.config.serialization.metadata.ConfigMetadata;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigFormat implements ConfigFormat<JsonElement> {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .serializeNulls()
            .setStrictness(Strictness.LENIENT)
            .enableComplexMapKeySerialization()
            .create();

    public static final JsonConfigFormat INSTANCE = new JsonConfigFormat(false);
    public static final JsonConfigFormat COMPRESSED = new JsonConfigFormat(true);
    public static final HashMap<ConfigType<?, ?>, Fixer<JsonElement>> FIXERS = new HashMap<>();

    protected final Gson gson;
    protected final boolean compressed;

    public JsonConfigFormat(Gson gson, boolean compressed) {
        this.gson = gson;
        this.compressed = compressed;
    }

    public JsonConfigFormat(boolean compressed) {
        this(GSON, compressed);
    }

    @Override
    public @NonNull DynamicOps<JsonElement> getOps() {
        return compressed ? JsonOps.COMPRESSED : JsonOps.INSTANCE;
    }

    @Override
    public <C, M extends ConfigMetadata> @NonNull JsonElement read(ConfigType<C, M> configType, Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return gson.fromJson(reader, JsonElement.class);
        }
    }

    @Override
    public <C, M extends ConfigMetadata> void write(ConfigType<C, M> configType, Path path, JsonElement element) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            gson.toJson(element, writer);
        }
    }

    @Override
    public @NonNull JsonElement getData(JsonElement rootElement) {
        return rootElement.getAsJsonObject().get(ConfigConstants.DATA_KEY);
    }

    @Override
    public @NonNull JsonElement getMetadata(JsonElement rootElement) {
        return rootElement.getAsJsonObject().get(ConfigConstants.METADATA_KEY);
    }

    @Override
    public @NonNull Map<ConfigType<?, ?>, Fixer<JsonElement>> getAvailableFixers() {
        return FIXERS;
    }

    @Override
    public @NonNull String suffix() {
        return "json";
    }
}