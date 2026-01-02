package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import net.minecraft.util.Identifier;

public class ConfigEnum<E extends Enum<E>> extends ConfigValue<E> {
    private final ConfigCodec<E> codec;
    private final Class<E> enumClass;

    public ConfigEnum(Identifier identifier, Class<E> enumClass, E e) {
        super(identifier, e);
        this.enumClass = enumClass;
        this.codec = ConfigCodec.enums(enumClass);
    }

    @Override
    public ConfigCodec<E> getCodec() {
        return this.codec;
    }

    public void next() {
        E[] values = enumClass.getEnumConstants();
        int index = get().ordinal();
        int next = (index + 1) % values.length;
        set(values[next]);
    }

    public void set(String name) {
        for (E val : enumClass.getEnumConstants()) {
            if (val.name().equalsIgnoreCase(name)) {
                set(val);
                return;
            }
        }
    }
}