package dev.proststuff.utilitary.api.config.field.value.minecraft;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import net.minecraft.resources.Identifier;

public class IdentifierConfigField extends ConfigField<Identifier> {
    public IdentifierConfigField(String name, Identifier value) {
        super(name, value, ConfigCodecs.IDENTIFIER);
    }
}
