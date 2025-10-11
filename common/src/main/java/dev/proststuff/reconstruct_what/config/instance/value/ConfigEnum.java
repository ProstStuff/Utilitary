package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.instance.ConfigCodec;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

public class ConfigEnum<E extends Enum<E>> extends ConfigValue<E> {
    private final Class<E> enumClass;

    public ConfigEnum(String name, Class<E> enumClass, E defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, ConfigCodec.enums(enumClass), runtimeOnly);
        this.enumClass = enumClass;
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

        ReconstructWhat.LOG.error("Unknown enum value of ConfigEnum: {} for {}", name, getName());
    }
}
