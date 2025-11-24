package dev.proststuff.utilitary.utility;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegistryUtil {
    public static <T> Identifier getIdentifierOrThrow(Registry<? super T> registry, T object) {
        Identifier id = registry.getId(object);
        if (id == null) {
            throw new NullPointerException("Object is not registered yet!");
        }

        return id;
    }
}
