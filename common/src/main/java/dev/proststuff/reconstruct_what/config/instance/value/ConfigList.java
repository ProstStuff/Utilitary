package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

import java.util.ArrayList;
import java.util.List;

public class ConfigList<T> extends AbstractConfigValue<List<T>> {
    public ConfigList(String name, boolean runtimeOnly) {
        super(name, new ArrayList<>(), runtimeOnly);
    }

    public ConfigList(String name, List<T> defaultValue, boolean runtimeOnly) {
        super(name, new ArrayList<>(defaultValue), runtimeOnly);
    }

    public void add(T element) {
        this.get().add(element);
        this.onChange();
    }

    public void remove(T element) {
        this.get().remove(element);
        this.onChange();
    }

    public void clear() {
        this.get().clear();
        this.onChange();
    }

    public boolean contains(T element) {
        return this.get().contains(element);
    }

    public int size() {
        return this.get().size();
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        JsonArray array = new JsonArray();

        for (T element : this.get()) {
            switch (element) {
                case null -> array.add(JsonNull.INSTANCE);
                case Number n -> array.add(new JsonPrimitive(n));
                case Boolean b -> array.add(new JsonPrimitive(b));
                case String s -> array.add(new JsonPrimitive(s));
                case ICanConfigure<?> configurable -> array.add(configurable.serialize(manager));
                default -> array.add(element.toString());
            }
        }

        return array;
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        if (element == null || !element.isJsonArray()) return;

        JsonArray jsonArray = element.getAsJsonArray();
        List<T> newList = new ArrayList<>();

        for (JsonElement item : jsonArray) {
            if (item == null || item.isJsonNull()) continue;

            T parsed = parseItem(item, manager);
            if (parsed != null) newList.add(parsed);
        }

        this.set(newList);
    }

    @SuppressWarnings("unchecked")
    private T parseItem(JsonElement element, ConfigManager manager) {
        try {
            if (!getDefault().isEmpty()) {
                Object first = getDefault().getFirst();
                if (first instanceof Integer) return (T) (Integer) element.getAsInt();
                if (first instanceof Float) return (T) (Float) element.getAsFloat();
                if (first instanceof Double) return (T) (Double) element.getAsDouble();
                if (first instanceof Boolean) return (T) (Boolean) element.getAsBoolean();
                if (first instanceof String) return (T) element.getAsString();

                if (first instanceof ICanConfigure<?> configurable) {
                    configurable.deserialize(element, manager);
                    return (T) configurable;
                }
            }

            if (element.isJsonPrimitive()) {
                JsonPrimitive p = element.getAsJsonPrimitive();
                if (p.isBoolean()) return (T) (Boolean) p.getAsBoolean();
                if (p.isNumber()) return (T) (Double) p.getAsDouble();
                if (p.isString()) return (T) p.getAsString();
            }

        } catch (Exception e) {
            manager.error(IFancyLogging.LogType.ERROR, "Failed to parse list element: {}", e);
        }

        return null;
    }
}
