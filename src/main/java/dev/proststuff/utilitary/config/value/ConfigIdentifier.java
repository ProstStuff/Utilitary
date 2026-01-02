package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

public class ConfigIdentifier extends ConfigValue<Identifier> {
    public ConfigIdentifier(Identifier identifier, Identifier value) {
        super(identifier, value);
    }

    @Override
    public ConfigCodec<Identifier> getCodec() {
        return ConfigCodecs.IDENTIFIER;
    }
}
