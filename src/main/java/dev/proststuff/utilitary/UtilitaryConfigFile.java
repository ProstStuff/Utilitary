package dev.proststuff.utilitary;

import dev.proststuff.utilitary.api.ConfigFile;
import dev.proststuff.utilitary.api.field.ConfigGroup;
import dev.proststuff.utilitary.api.field.value.math.ColorConfigField;
import dev.proststuff.utilitary.api.field.value.BooleanConfigField;
import dev.proststuff.utilitary.api.field.value.StringConfigField;
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

    public UtilitaryConfigFile() {
        super(Identifier.fromNamespaceAndPath(Utilitary.ID, "preview"));
        add(
                configurationEnabled,
                information,
                group
        );
    }
}
