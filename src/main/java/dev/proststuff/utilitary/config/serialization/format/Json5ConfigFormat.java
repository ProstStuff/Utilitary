package dev.proststuff.utilitary.config.serialization.format;

import com.mojang.serialization.DynamicOps;
import de.marhali.json5.Json5;
import de.marhali.json5.Json5Element;
import de.marhali.json5.Json5Object;
import de.marhali.json5.config.DuplicateKeyStrategy;
import dev.proststuff.utilitary.config.ConfigConstants;
import dev.proststuff.utilitary.config.ConfigType;
import dev.proststuff.utilitary.config.serialization.Comment;
import dev.proststuff.utilitary.config.serialization.format.ops.Json5Ops;
import dev.proststuff.utilitary.config.serialization.metadata.ConfigMetadata;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Json5ConfigFormat implements ConfigFormat<Json5Element> {
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

    public static final Json5ConfigFormat INSTANCE = new Json5ConfigFormat(false);
    public static final Json5ConfigFormat COMPRESSED = new Json5ConfigFormat(true);
    public static final HashMap<ConfigType<?, ?>, Fixer<Json5Element>> FIXERS = new HashMap<>();

    protected final Json5 json5;
    protected final boolean compressed;

    public Json5ConfigFormat(Json5 json5, boolean compressed) {
        this.json5 = json5;
        this.compressed = compressed;
    }

    public Json5ConfigFormat(boolean compressed) {
        this(JSON5, compressed);
    }

    @Override
    public @NonNull DynamicOps<Json5Element> getOps() {
        return compressed ? Json5Ops.COMPRESSED : Json5Ops.INSTANCE;
    }

    @Override
    public <C, M extends ConfigMetadata> @NonNull Json5Element read(ConfigType<C, M> configType, Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return json5.parse(reader);
        }
    }

    @Override
    public <C, M extends ConfigMetadata> void write(ConfigType<C, M> configType, Path path, Json5Element element) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            Json5Object object = element.getAsJson5Object();
            object.add(ConfigConstants.METADATA_KEY, comments(object.remove(ConfigConstants.METADATA_KEY), configType.metadata().comment()));
            object.add(ConfigConstants.DATA_KEY, comments(object.remove(ConfigConstants.DATA_KEY), configType.comments()));
            json5.serialize(object, writer);
        }
    }

    @Override
    public @NonNull Json5Element getData(Json5Element rootElement) {
        return rootElement.getAsJson5Object().get(ConfigConstants.DATA_KEY);
    }

    @Override
    public @NonNull Json5Element getMetadata(Json5Element rootElement) {
        return rootElement.getAsJson5Object().get(ConfigConstants.METADATA_KEY);
    }

    @Override
    public @NonNull Map<ConfigType<?, ?>, Fixer<Json5Element>> getAvailableFixers() {
        return FIXERS;
    }

    @Override
    public @NonNull String suffix() {
        return "json5";
    }

    public static <E extends Json5Element> E comments(E element, Comment comment) {
        if (element.isJson5Object()) {
            for (Map.Entry<String, Json5Element> entry : element.getAsJson5Object().entrySet()) {
                comments(entry.getValue(), comment.get(entry.getKey()));
            }
        }

        element.setComment(comment.getNonEmptyString());
        return element;
    }
}