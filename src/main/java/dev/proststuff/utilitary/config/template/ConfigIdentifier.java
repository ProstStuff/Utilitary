package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;
import net.minecraft.util.Identifier;

public class ConfigIdentifier extends ConfigValue<Identifier> {
    public ConfigIdentifier(String name, Identifier value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<Identifier> getCodec() {
        return ConfigCodec.IDENTIFIER;
    }
}
