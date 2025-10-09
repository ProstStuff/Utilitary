package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.resources.ResourceLocation;

public class ConfigResourceLocation extends AbstractConfigValue<ResourceLocation> {

    public ConfigResourceLocation(String name, ResourceLocation defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public ResourceLocation set(ResourceLocation value) {
        return super.set(value != null ? value : getDefault());
    }

    public String asString() {
        return get().toString();
    }

    public String getNamespace() {
        return get().getNamespace();
    }

    public String getPath() {
        return get().getPath();
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        return new JsonPrimitive(this.value.toString());
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        try {
            ResourceLocation parse = ResourceLocation.tryParse(element.getAsString());

            if (parse != null) {
                this.set(parse);
            } else {
                manager.warn(IFancyLogging.LogType.WARN, "ResourceLocation is not valid, fallback to default.");
                this.setDefault();
            }
        } catch (Exception e) {
            this.setDefault();
            manager.warn(IFancyLogging.LogType.WARN, "Exception found. Fallback {} value to default.", this.defaultValue.getClass().getName());
        }
    }
}