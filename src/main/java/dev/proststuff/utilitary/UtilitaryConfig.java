package dev.proststuff.utilitary;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.proststuff.utilitary.api.v0.utility.SimpleIdentifier;
import dev.proststuff.utilitary.config.v0.ConfigCodec;
import dev.proststuff.utilitary.config.v0.ConfigManager;
import dev.proststuff.utilitary.config.v0.ConfigResult;
import dev.proststuff.utilitary.config.v0.ConfigType;
import dev.proststuff.utilitary.config.v0.serialization.Comment;
import dev.proststuff.utilitary.config.v0.serialization.format.Json5ConfigFormat;
import dev.proststuff.utilitary.config.v0.serialization.metadata.SimpleMetadataType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public record UtilitaryConfig(boolean debugLogging, boolean safeMode) {
    public static final SimpleIdentifier ID = SimpleIdentifier.of(Utilitary.ID, "config");
    public static final ConfigCodec<UtilitaryConfig> CODEC = ConfigCodec.of(
            RecordCodecBuilder.create(
                    (inst) -> inst.group(
                            Codec.BOOL.fieldOf("debugLogging").forGetter(UtilitaryConfig::debugLogging),
                            Codec.BOOL.fieldOf("safeMode").forGetter(UtilitaryConfig::safeMode)
                    ).apply(inst, UtilitaryConfig::new)
            )
    );

    public static final ConfigType<UtilitaryConfig, SimpleMetadataType.Metadata, Json5ConfigFormat.FormatSettings> TYPE = new ConfigType<>(
            Identifier.fromNamespaceAndPath(Utilitary.ID, "config"),
            1,
            CODEC,
            (_) -> new UtilitaryConfig(false, false),
            SimpleMetadataType.INSTANCE,
            Json5ConfigFormat.INSTANCE,
            Comment.of(Component.translatable("utilitary.config"))
                    .addComment("debugLogging", Component.translatable("utilitary.config.debug_printing.info"))
                    .addComment("safeMode", Component.translatable("utilitary.config.safe_mode.info"))
    );

    public static UtilitaryConfig load() {
        ConfigResult<UtilitaryConfig, SimpleMetadataType.Metadata> result = ConfigManager.load(UtilitaryConfig.ID, UtilitaryConfig.TYPE);
        return result.config();
    }

    public static void save() {
        ConfigManager.save(UtilitaryConfig.ID, UtilitaryConfig.TYPE, Utilitary.CONFIG);
    }

    static {
        List<ConfigType.Migration> migrationList = TYPE.migrations();

        migrationList.add((context) -> {
            JsonObject object = context.data().getAsJsonObject();
            object.add("debugLogging", object.remove("debug"));
            return object;
        });
    }
}