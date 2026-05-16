package dev.proststuff.utilitary.serialization.content.field.math;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;
import org.joml.Vector3d;

public class Vector3dConfigField extends ConfigField<Vector3d> {
    public Vector3dConfigField(String name, Vector3d value) {
        super(name, value, ConfigCodecs.VECTOR3D);
    }
}
