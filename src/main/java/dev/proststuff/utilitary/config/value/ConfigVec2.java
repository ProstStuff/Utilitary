package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import dev.proststuff.utilitary.config.utility.ConfigCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public class ConfigVec2 extends ConfigValue<Vec2f> {
    public ConfigVec2(Identifier identifier, Vec2f value) {
        super(identifier, value);
    }

    @Override
    public ConfigCodec<Vec2f> getCodec() {
        return ConfigCodecs.VECTOR2;
    }

    public float x() {
        return get().x;
    }

    public float y() {
        return get().y;
    }
}
