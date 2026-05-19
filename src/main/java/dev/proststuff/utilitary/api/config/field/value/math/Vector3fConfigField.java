package dev.proststuff.utilitary.api.config.field.value.math;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import org.joml.Vector3f;

public class Vector3fConfigField extends ConfigField<Vector3f> {
    public Vector3fConfigField(String name, Vector3f value) {
        super(name, value, ConfigCodecs.VECTOR3F);
    }
}
