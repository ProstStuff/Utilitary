package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T> RegistryEntry<T> register(Registry<? super T> registry, Identifier id, Supplier<T> value) {
        return new RegistryEntry<>(id, registry, Registry.register(registry, id, value.get()));
    }
}
