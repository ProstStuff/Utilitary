package dev.proststuff.reconstruct_what.config.adapter;

import com.google.gson.*;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.instance.ConfigGroup;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;
import dev.proststuff.reconstruct_what.config.ICanConfigure;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * I forgot what this does...
 * Wait did I forgot I made this?
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

    public static void applyToGroup(ConfigGroup group, JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            Object obj = group.get(entry.getKey());
            JsonElement value = entry.getValue();

            if (obj instanceof ConfigValue<?> base) {
                try {
                    Object val = new Gson().fromJson(value, base.get().getClass());
                    applyToBase(base, val);
                } catch (Exception e) {
                    ReconstructWhat.LOG.error("Exception while loading {}: {}", base.getName(), e.getMessage());
                }
            } else if (obj instanceof ConfigGroup subGroup && value.isJsonObject()) {
                applyToGroup(subGroup, value.getAsJsonObject());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void applyToBase(ConfigValue<T> base, Object value) {
        try {
            base.set((T) value);
        } catch (ClassCastException e) {
            ReconstructWhat.LOG.error("Type mistmatch while loading {}: {}", base.getName(), e.getMessage());
        }
    }
}