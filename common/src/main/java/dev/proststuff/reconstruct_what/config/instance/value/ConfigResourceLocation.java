package dev.proststuff.reconstruct_what.config.instance.value;

import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import net.minecraft.resources.ResourceLocation;

public class ConfigResourceLocation extends ConfigValue<ResourceLocation> {
    public ConfigResourceLocation(String name, ResourceLocation resourceLocation, boolean runtimeOnly) {
        super(name, resourceLocation, ConfigCodecs.RESOURCE_LOCATION, runtimeOnly);
    }

    public ConfigResourceLocation(String name, ResourceLocation resourceLocation) {
        this(name, resourceLocation, false);
    }

    @Override
    public ResourceLocation set(ResourceLocation value) {
        return super.set(value != null ? value : getDefault());
    }

    @Override
    public boolean is(Object obj) {
        if (obj instanceof ResourceLocation resourceLocation) {
            return resourceLocation.equals(get());
        }

        return super.is(obj);
    }

    public String getNamespace() {return get().getNamespace();}
    public String getPath() {return get().getPath();}
}