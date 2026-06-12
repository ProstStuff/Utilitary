package dev.proststuff.utilitary.api.config.field.value.minecraft;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import net.minecraft.world.phys.Vec3;

public class Vec3ConfigField extends ConfigField<Vec3> {
    public Vec3ConfigField(String name, Vec3 value) {
        super(name, value, ConfigCodecs.VEC3);
    }

    public void setX(double x) {
        set(get().add(x, 0, 0));
    }

    public void setY(double y) {
        set(get().add(0, y, 0));
    }

    public void setZ(double z) {
        set(get().add(0, 0, z));
    }
}
