package dev.proststuff.utilitary.serialization.content.field.minecraft;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;
import net.minecraft.resources.Identifier;

public class IdentifierConfigField extends ConfigField<Identifier> {
    public IdentifierConfigField(String name, Identifier value) {
        super(name, value, ConfigCodecs.IDENTIFIER);
    }
}
