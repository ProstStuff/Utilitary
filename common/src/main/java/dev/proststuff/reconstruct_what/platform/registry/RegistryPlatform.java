package dev.proststuff.reconstruct_what.platform.registry;

import dev.proststuff.reconstruct_what.platform.AbstractPlatform;
import net.minecraft.core.Registry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class RegistryPlatform<T> {
    protected final AbstractPlatform PLATFORM;
    protected final Registry<? super T> REGISTRY;
    protected final BiFunction<String, Supplier<? extends T>, Supplier<? extends T>> registrationFunction;

    protected Set<RegistryEntry<? extends T>> ENTRIES = new HashSet<>();

    public RegistryPlatform(AbstractPlatform platform, Registry<? super T> registry) {
        this.PLATFORM = platform;
        this.REGISTRY = registry;

        this.registrationFunction = PLATFORM.createRegistrationFunction(REGISTRY);
    }

    public <O extends T> RegistryEntry<O> register(String name, Supplier<O> supplier) {
        if (registrationFunction == null) throw new RuntimeException(parseAsString(this) + " doesn't know how to register their objects. `setRegistration` must be called first, function is platform dependent.");
        if (getEntry(name) != null) throw new RuntimeException(parseAsString(this) + " already registered " + name + ", they have the same name!");

        RegistryEntry<O> entry = new RegistryEntry<>(PLATFORM, name, (Registry<O>) REGISTRY, (Supplier<O>) registrationFunction.apply(name, supplier));
        ENTRIES.add(entry);
        return entry;
    }

    public RegistryEntry<?> getEntry(String name) {
        for (RegistryEntry<? extends T> entry : ENTRIES) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }

        return null;
    }

    public Registry<? super T> getRegistry() {return REGISTRY;}
    public Set<RegistryEntry<? extends T>> getEntries() {return ENTRIES;}

    public static <T> String parseAsString(RegistryPlatform<T> platform) {return platform.PLATFORM.getModId() + "-" + platform.REGISTRY.key().location();}

    // All objects must be registered here
    public void init() {}
}