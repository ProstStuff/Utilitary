package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.util.Phantom;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public record RegistryEntry<T>(Identifier id, Registry<? super T> registry, Supplier<T> value) implements Phantom<T> {
    public RegistryEntry(Identifier id, Registry<? super T> registry, T value) {
        this(id, registry, () -> value);
    }

    @Override
    public T get() {
        return value.get();
    }
}