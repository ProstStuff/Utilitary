package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class ConfigVec3 extends ConfigValue<Vec3d> {
    public ConfigVec3(Identifier identifier, Vec3d value) {
        super(identifier, value);
    }

    @Override
    public ConfigCodec<Vec3d> getCodec() {
        return ConfigCodecs.VECTOR3;
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
