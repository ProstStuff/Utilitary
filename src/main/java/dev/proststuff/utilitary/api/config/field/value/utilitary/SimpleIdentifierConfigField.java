package dev.proststuff.utilitary.api.config.field.value.utilitary;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;

public class SimpleIdentifierConfigField extends ConfigField<SimpleIdentifier> {
    public SimpleIdentifierConfigField(String name, SimpleIdentifier value) {
        super(name, value, ConfigCodecs.SIMPLE_IDENTIFIER);
    }
}
