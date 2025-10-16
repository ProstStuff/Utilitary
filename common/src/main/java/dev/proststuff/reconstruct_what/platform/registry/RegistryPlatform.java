package dev.proststuff.reconstruct_what.platform.registry;

import dev.proststuff.reconstruct_what.platform.services.AbstractPlatform;
import net.minecraft.core.Registry;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class RegistryPlatform<T> {
    protected AbstractPlatform PLATFORM;
    protected Registry<? super T> REGISTRY;

    protected BiFunction<String, Supplier<? extends T>, Supplier<? extends T>> registrationFunction;

    public RegistryPlatform(AbstractPlatform platform, Registry<? super T> registry) {
        this.PLATFORM = platform;
        this.REGISTRY = registry;
    }

    public void setRegistration(BiFunction<String, Supplier<? extends T>, Supplier<? extends T>> func) {
        if (this.registrationFunction != null) throw new RuntimeException(parseAsString(this) + " already know how to register their objects! `setRegistration` can only be called once!");
        this.registrationFunction = func;
    }

    public <O extends T> RegistryEntry<O> register(String name, Supplier<O> supplier) {
        if (registrationFunction == null) throw new RuntimeException(parseAsString(this) + " doesn't know how to register their objects. `setRegistration` must be called first, function is platform dependent.");
        return new RegistryEntry<>(PLATFORM, name, (Registry<O>) REGISTRY, (Supplier<O>) registrationFunction.apply(name, supplier));
    }

    public Registry<? super T> getRegistry() {
        return REGISTRY;
    }

    public static <T> String parseAsString(RegistryPlatform<T> platform) {
        return platform.PLATFORM.getModId() + "-" + platform.REGISTRY.key().location();
    }
}