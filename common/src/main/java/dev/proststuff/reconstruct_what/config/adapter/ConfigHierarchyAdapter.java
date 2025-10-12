package dev.proststuff.reconstruct_what.config.adapter;

import com.google.gson.*;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.config.instance.ConfigGroup;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * I forgot what this does...
 * Oh, it's for ConfigGroup nesting...
 */
public class ConfigHierarchyAdapter implements JsonSerializer<ICanConfigure<?>>, JsonDeserializer<ICanConfigure<?>> {
    @Override
    public JsonElement serialize(ICanConfigure<?> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof ConfigValue<?> base) {
            return context.serialize(base.get());
        }

        if (src instanceof ConfigGroup group) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, ICanConfigure<?>> entry : group.getEntries().entrySet()) {
                ICanConfigure<?> cfg = entry.getValue();

                obj.add(entry.getKey(), serialize(cfg, cfg.getClass(), context));
            }

            return obj;
        }

        return JsonNull.INSTANCE;
    }
    @Override
    public ICanConfigure<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        throw new UnsupportedOperationException("Deserialization is handled manually in ConfigHolder.loadAll()");
    }
}