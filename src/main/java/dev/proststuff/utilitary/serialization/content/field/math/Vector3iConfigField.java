package dev.proststuff.utilitary.serialization.content.field.math;

import dev.proststuff.utilitary.serialization.codec.ConfigCodecs;
import dev.proststuff.utilitary.serialization.content.ConfigField;
import org.joml.Vector3i;

public class Vector3iConfigField extends ConfigField<Vector3i> {
    public Vector3iConfigField(String name, Vector3i value) {
        super(name, value, ConfigCodecs.VECTOR3I);
    }
}
