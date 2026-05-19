package dev.proststuff.utilitary.api.config.field.value.math;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import org.joml.Vector3d;

public class Vector3dConfigField extends ConfigField<Vector3d> {
    public Vector3dConfigField(String name, Vector3d value) {
        super(name, value, ConfigCodecs.VECTOR3D);
    }
}
