package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

public class ConfigBoolean extends ConfigValue<Boolean> {
    public ConfigBoolean(Identifier identifier, Boolean value) {
        super(identifier, value);
    }

    @Override
    public ConfigCodec<Boolean> getCodec() {
        return ConfigCodecs.BOOLEAN;
    }

    public boolean toggle() {
        return set(!get());
    }
}
