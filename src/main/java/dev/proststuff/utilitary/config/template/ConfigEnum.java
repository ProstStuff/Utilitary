package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

public class ConfigEnum<E extends Enum<E>> extends ConfigValue<E> {
    private final ConfigCodec<E> codec;
    private final Class<E> enumClass;

    public ConfigEnum(String name, Class<E> enumClass, E e) {
        super(name, e);
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

        getConfigManager().warn("Unknown enum value of ConfigEnum: {} for {}", name, getName());
    }
}