package dev.proststuff.utilitary;

import dev.proststuff.utilitary.api.config.ConfigFile;
import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigGroup;
import dev.proststuff.utilitary.api.config.field.value.BooleanConfigField;
import dev.proststuff.utilitary.api.config.field.value.MapConfigField;
import dev.proststuff.utilitary.api.config.field.value.StringConfigField;
import dev.proststuff.utilitary.api.config.field.value.utilitary.ColorConfigField;
import net.minecraft.resources.Identifier;

public class UtilitaryConfigFile extends ConfigFile {
    public static final UtilitaryConfigFile INSTANCE = new UtilitaryConfigFile();

    public final BooleanConfigField configurationEnabled = new BooleanConfigField("configurationEnabled", true);
    public final StringConfigField information = new StringConfigField("information", "500 configurations.");
    public final ConfigGroup group = new ConfigGroup("utilitary").add(
            new BooleanConfigField("groupingEnabled", true),
            new StringConfigField("grouping", "Infinitely grouped. (Please don't loop this)"),
            new ColorConfigField("color", 0xFFFFFF)
    );
    public final MapConfigField<String> map = new MapConfigField<>("map", ConfigCodecs.STRING);

    public UtilitaryConfigFile() {
        super(Identifier.fromNamespaceAndPath(Utilitary.ID, "preview"));
        map.put("s", "a");

        add(
                configurationEnabled,
                information,
                group,
                map
        );
    }
}
