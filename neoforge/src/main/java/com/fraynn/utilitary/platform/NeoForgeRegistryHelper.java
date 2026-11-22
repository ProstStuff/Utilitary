package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    private static final Map<String, Map<Registry<?>, DeferredRegister<?>>> REGISTRIES = new HashMap<>();

    public void registerFor(String modId, IEventBus eventBus) {
        Map<Registry<?>, DeferredRegister<?>> registrations = REGISTRIES.get(modId);
        if (registrations == null) return;
        registrations.values().forEach(deferredRegister -> deferredRegister.register(eventBus));
    }

    @SuppressWarnings("unchecked")
    public <T> DeferredRegister<T> getOrCreateRegister(String modId, Registry<T> registry) {
        return (DeferredRegister<T>) REGISTRIES
                .computeIfAbsent(modId, (_) -> new HashMap<>())
                .computeIfAbsent(registry, (_) -> DeferredRegister.create(registry, modId));
    }

    @Override
    public <T> RegistryEntry<T> register(Registry<? super T> registry, Identifier id, Supplier<T> value) {
        DeferredHolder<? super T, T> holder = getOrCreateRegister(id.getNamespace(), registry).register(id.getPath(), value);
        return new RegistryEntry<>(id, registry, holder);
    }
}
