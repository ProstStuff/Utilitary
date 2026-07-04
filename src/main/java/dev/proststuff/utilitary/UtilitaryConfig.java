package dev.proststuff.utilitary;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.proststuff.utilitary.api.config.ConfigCodec;
import dev.proststuff.utilitary.api.config.ConfigManager;
import dev.proststuff.utilitary.api.config.ConfigType;
import dev.proststuff.utilitary.api.config.serialization.metadata.VersionOnlyConfigMetadataType;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;
import net.minecraft.resources.Identifier;

import java.util.NavigableMap;
import java.util.TreeMap;

public record UtilitaryConfig(boolean safeMode, boolean debugPrinting) {
    public static final SimpleIdentifier ID = SimpleIdentifier.of(Utilitary.ID, "config");
    public static final Codec<UtilitaryConfig> CODEC = RecordCodecBuilder.create(
            (inst) -> inst.group(
                    Codec.BOOL.fieldOf("safeMode").forGetter(UtilitaryConfig::safeMode),
                    Codec.BOOL.fieldOf("debugPrinting").forGetter(UtilitaryConfig::debugPrinting)
            ).apply(inst, UtilitaryConfig::new)
    );

    /*
    public static final StreamCodec<RegistryFriendlyByteBuf, UtilitaryConfig> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UtilitaryConfig::safeMode,
            ByteBufCodecs.BOOL, UtilitaryConfig::debugPrinting,
            UtilitaryConfig::new
    );*/

    public static final ConfigType<UtilitaryConfig, VersionOnlyConfigMetadataType.Metadata, VersionOnlyConfigMetadataType> TYPE;

    static {
        NavigableMap<Integer, ConfigType.Migration> migrations = new TreeMap<>();

        migrations.put(1, (context) -> {
            JsonObject object = context.data().getAsJsonObject();
            object.add("debugPrinting", object.remove("debug"));
            return object;
        });

        TYPE = ConfigManager.register(new ConfigType<>(
                Identifier.fromNamespaceAndPath(Utilitary.ID, "config_type"),
                2,
                ConfigCodec.of(CODEC, null),
                (_) -> new UtilitaryConfig(false, false),
                migrations,
                new VersionOnlyConfigMetadataType()
        ));
    }
}