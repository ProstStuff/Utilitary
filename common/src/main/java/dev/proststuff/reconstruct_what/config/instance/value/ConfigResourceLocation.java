package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import net.minecraft.resources.ResourceLocation;

public class ConfigResourceLocation extends ConfigValue<ResourceLocation> {
    public ConfigResourceLocation(String name, ResourceLocation defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, ConfigCodecs.RESOURCE_LOCATION, runtimeOnly);
    }

    public ConfigResourceLocation(String name, ResourceLocation defaultValue) {
        this(name, defaultValue, false);
    }

    @Override
    public ResourceLocation set(ResourceLocation value) {
        return super.set(value != null ? value : getDefault());
    }

    public String getNamespace() {
        return get().getNamespace();
    }

    public String getPath() {
        return get().getPath();
    }

    @Override
    public boolean is(Object obj) {
        if (obj instanceof ResourceLocation resourceLocation) {
            return resourceLocation.equals(get());
        }

        return super.is(obj);
    }
}