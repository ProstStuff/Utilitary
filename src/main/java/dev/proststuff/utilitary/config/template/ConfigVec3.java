package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class ConfigVec3 extends ConfigValue<Vec3d> {
    public ConfigVec3(String name, Vec3d value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<Vec3d> getCodec() {
        return ConfigCodec.VECTOR3;
    }

    public Vec3i getAsInteger() {
        return new Vec3i((int) get().x, (int) get().y, (int) get().z);
    }

    public double x() {
        return get().x;
    }

    public double y() {
        return get().y;
    }

    public double z() {
        return get().z;
    }
}
