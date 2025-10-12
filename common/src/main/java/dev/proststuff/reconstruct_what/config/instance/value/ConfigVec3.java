package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import net.minecraft.world.phys.Vec3;

public class ConfigVec3 extends ConfigValue<Vec3> {
    public ConfigVec3(String name, Vec3 vec3, boolean runtimeOnly) {
        super(name, vec3, ConfigCodecs.VEC3, runtimeOnly);
    }

    public ConfigVec3(String name, Vec3 vec3) {
        this(name, vec3, false);
    }

    public ConfigVec3(String name, boolean runtimeOnly) {
        this(name, new Vec3(0, 0, 0), runtimeOnly);
    }

    public ConfigVec3(String name) {
        this(name, false);
    }
}