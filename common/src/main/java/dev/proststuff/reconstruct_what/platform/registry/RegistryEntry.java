package dev.proststuff.reconstruct_what.platform.registry;

import dev.proststuff.reconstruct_what.platform.AbstractPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class RegistryEntry<T> {
    private final AbstractPlatform platform;
    private final String name;
    private final Registry<T> registry;
    private final Supplier<T> supplier;

    private final ResourceLocation identifier;
    private final ResourceKey<T> resourceKey;
    private List<String> exclusives;

    private boolean locked = false;

    public RegistryEntry(AbstractPlatform platform, String name, Registry<T> registry, Supplier<T> supplier) {
        this.name = name;
        this.registry = registry;
        this.supplier = supplier;

        this.identifier = ResourceLocation.fromNamespaceAndPath(platform.getModId(), name);
        this.resourceKey = ResourceKey.create(registry.key(), identifier);
        this.platform = platform;
    }

    public RegistryEntry<T> exclusiveTo(String modId) {
        if (exclusives == null) {this.exclusives = List.of();}
        exclusives.clear();
        exclusives.add(modId);
        return this;
    }

    public RegistryEntry<T> exclusiveTo(String... modIds) {
        if (exclusives == null) {this.exclusives = List.of();}
        exclusives.clear();
        exclusives.addAll(Arrays.asList(modIds));
        return this;
    }

    public void lock(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        if (exclusives != null && !exclusives.isEmpty()) {
            for (String modId : exclusives) {
                if (platform.isModLoaded(modId)) return false;
            }
            return true;
        }

        return locked;
    }

    public T get() {return supplier.get();}
    public String getName() {return name;}
    public ResourceLocation getIdentifier() {return identifier;}
    public Registry<T> getRegistry() {return registry;}
    public Holder<T> getHolder() {return registry.getHolderOrThrow(this.resourceKey);}
}