package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

public class ConfigString extends ConfigValue<String> {
    public ConfigString(Identifier identifier, String value) {
        super(identifier, value);
    }

    @Override
    public ConfigCodec<String> getCodec() {
        return ConfigCodecs.STRING;
    }

    public boolean isEmpty() {
        return get().isEmpty();
    }
}
