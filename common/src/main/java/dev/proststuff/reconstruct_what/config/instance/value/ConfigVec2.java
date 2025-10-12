package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import net.minecraft.world.phys.Vec2;

public class ConfigVec2 extends ConfigValue<Vec2> {
    public ConfigVec2(String name, Vec2 vec2, boolean runtimeOnly) {
        super(name, vec2, ConfigCodecs.VEC2, runtimeOnly);
    }

    public ConfigVec2(String name, Vec2 vec2) {
        this(name, vec2, false);
    }

    public ConfigVec2(String name, boolean runtimeOnly) {
        this(name, new Vec2(0, 0), runtimeOnly);
    }

    public ConfigVec2(String name) {
        this(name, false);
    }
}