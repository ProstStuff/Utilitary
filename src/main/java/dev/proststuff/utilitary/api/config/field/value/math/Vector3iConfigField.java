package dev.proststuff.utilitary.api.config.field.value.math;

import dev.proststuff.utilitary.api.config.codec.ConfigCodecs;
import dev.proststuff.utilitary.api.config.field.ConfigField;
import org.joml.Vector3i;

public class Vector3iConfigField extends ConfigField<Vector3i> {
    public Vector3iConfigField(String name, Vector3i value) {
        super(name, value, ConfigCodecs.VECTOR3I);
    }
}
