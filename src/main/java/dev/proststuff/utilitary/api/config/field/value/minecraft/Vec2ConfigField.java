package dev.proststuff.utilitary.api.config.field.value.minecraft;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import net.minecraft.world.phys.Vec2;

public class Vec2ConfigField extends ConfigField<Vec2> {
    public Vec2ConfigField(String name, Vec2 value) {
        super(name, value, ConfigCodecs.VEC2);
    }
}
