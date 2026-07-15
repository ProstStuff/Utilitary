package dev.proststuff.utilitary;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.proststuff.utilitary.api.config.ConfigCodec;
import dev.proststuff.utilitary.api.config.ConfigType;
import dev.proststuff.utilitary.api.config.serialization.Comment;
import dev.proststuff.utilitary.api.config.serialization.format.Json5ConfigFormat;
import dev.proststuff.utilitary.api.config.serialization.metadata.SimpleMetadataType;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.resources.Identifier;

import java.util.NavigableMap;
import java.util.TreeMap;

public record UtilitaryConfig(boolean safeMode, boolean debugPrinting) {
    public static final SimpleIdentifier ID = SimpleIdentifier.of(Utilitary.ID, "config");
    public static final ConfigCodec<UtilitaryConfig> CODEC = ConfigCodec.of(
            RecordCodecBuilder.create(
                    (inst) -> inst.group(
                            Codec.BOOL.fieldOf("safeMode").forGetter(UtilitaryConfig::safeMode),
                            Codec.BOOL.fieldOf("debugPrinting").forGetter(UtilitaryConfig::debugPrinting)
                    ).apply(inst, UtilitaryConfig::new)
            )
    );

    public static final ConfigType<UtilitaryConfig, SimpleMetadataType.Metadata, Json5ConfigFormat.FormatSettings> TYPE;
    public static final Json5ConfigFormat.FormatSettings FORMAT_SETTINGS;

    static {
        NavigableMap<Integer, ConfigType.Migration> migrations = new TreeMap<>();

        migrations.put(1, (context) -> {
            JsonObject object = context.data().getAsJsonObject();
            object.add("debugPrinting", object.remove("debug"));
            return object;
        });

        TYPE = new ConfigType<>(
                Identifier.fromNamespaceAndPath(Utilitary.ID, "config"),
                2,
                CODEC,
                (_) -> new UtilitaryConfig(false, false),
                migrations,
                SimpleMetadataType.INSTANCE,
                Json5ConfigFormat.INSTANCE
        );

        Comment comment = Comment.of("Utilitary Configuration");
        comment.addComment("safeMode", "Disable saving and loading config file. Loading config file always returns default");
        comment.addComment("debugPrinting", "Show all Utilitary debug message that's marked as [INFO]");
        FORMAT_SETTINGS = Json5ConfigFormat.withComment(TYPE, comment);
    }
}