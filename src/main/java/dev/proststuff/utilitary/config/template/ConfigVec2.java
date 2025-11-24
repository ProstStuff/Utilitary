package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;
import net.minecraft.util.math.Vec2f;

public class ConfigVec2 extends ConfigValue<Vec2f> {
    public ConfigVec2(String name, Vec2f value) {
        super(name, value);
    }

    @Override
    public ConfigCodec<Vec2f> getCodec() {
        return ConfigCodec.VECTOR2;
    }

    public float x() {
        return get().x;
    }

    public float y() {
        return get().y;
    }
}
