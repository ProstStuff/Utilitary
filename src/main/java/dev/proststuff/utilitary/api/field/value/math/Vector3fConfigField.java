package dev.proststuff.utilitary.api.field.value.math;

import dev.proststuff.utilitary.api.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.field.ConfigField;
import org.joml.Vector3f;

public class Vector3fConfigField extends ConfigField<Vector3f> {
    public Vector3fConfigField(String name, Vector3f value) {
        super(name, value, ConfigCodecs.VECTOR3F);
    }
}
