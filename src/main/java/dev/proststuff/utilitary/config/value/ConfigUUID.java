package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ConfigUUID extends ConfigValue<UUID> {
    public ConfigUUID(Identifier identifier, UUID value) {
        super(identifier, value);
    }

    @Override
    public ConfigCodec<UUID> getCodec() {
        return ConfigCodecs.UUID;
    }
}
