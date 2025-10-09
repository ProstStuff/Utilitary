package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.config.instance.AbstractConfigValue;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ConfigMap<K, V> extends AbstractConfigValue<Map<K, V>> {
    public ConfigMap(String name, boolean runtimeOnly) {
        super(name, new LinkedHashMap<>(), runtimeOnly);
    }

    public ConfigMap(String name, Map<K, V> defaultValue, boolean runtimeOnly) {
        super(name, defaultValue, runtimeOnly);
    }

    public void put(K key, V value) {
        get().put(key, value);
        onChange();
    }

    public V get(K key) {
        return get().get(key);
    }

    public boolean containsKey(K key) {
        return get().containsKey(key);
    }

    public void remove(K key) {
        get().remove(key);
        onChange();
    }

    public void clear() {
        get().clear();
        onChange();
    }

    public void forEach(BiConsumer<K, V> action) {
        get().forEach(action);
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<K, V> entry : get().entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (key == null) continue;

            String keyStr = keyToString(key);

            switch (value) {
                case null -> {
                    jsonObject.add(keyStr, JsonNull.INSTANCE);
                }
                case Number n -> jsonObject.add(keyStr, new JsonPrimitive(n));
                case Boolean b -> jsonObject.add(keyStr, new JsonPrimitive(b));
                case String s -> jsonObject.add(keyStr, new JsonPrimitive(s));
                case ICanConfigure<?> configurable -> jsonObject.add(keyStr, configurable.serialize(manager));
                default -> jsonObject.add(keyStr, new JsonPrimitive(value.toString()));
            }

        }

        return jsonObject;
    }

    private String keyToString(K key) {
        if (key instanceof String s) return s;
        if (key instanceof Number n) return n.toString();
        if (key instanceof Boolean b) return b.toString();
        return String.valueOf(key);
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        if (element == null || !element.isJsonObject()) return;
        JsonObject json = element.getAsJsonObject();

        Map<K, V> newMap = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String keyStr = entry.getKey();
            JsonElement valueEl = entry.getValue();

            K key = parseKey(keyStr);
            if (key == null) continue;

            V value = parseValue(valueEl, manager);
            if (value != null) newMap.put(key, value);
        }

        set(newMap);
    }

    @SuppressWarnings("unchecked")
    private K parseKey(String keyStr) {
        if (!getDefault().isEmpty()) {
            K firstKey = getDefault().keySet().iterator().next();
            if (firstKey instanceof Integer) return (K) Integer.valueOf(keyStr);
            if (firstKey instanceof Float) return (K) Float.valueOf(keyStr);
            if (firstKey instanceof Double) return (K) Double.valueOf(keyStr);
            if (firstKey instanceof Boolean) return (K) Boolean.valueOf(keyStr);
            if (firstKey instanceof String) return (K) keyStr;
        }
        return (K) keyStr;
    }

    @SuppressWarnings("unchecked")
    private V parseValue(JsonElement element, ConfigManager manager) {
        try {
            if (!getDefault().isEmpty()) {
                V firstValue = getDefault().values().iterator().next();

                if (firstValue instanceof Integer) return (V) (Integer) element.getAsInt();
                if (firstValue instanceof Float) return (V) (Float) element.getAsFloat();
                if (firstValue instanceof Double) return (V) (Double) element.getAsDouble();
                if (firstValue instanceof Boolean) return (V) (Boolean) element.getAsBoolean();
                if (firstValue instanceof String) return (V) element.getAsString();

                if (firstValue instanceof ICanConfigure<?> configurable) {
                    configurable.deserialize(element, manager);
                    return (V) configurable;
                }
            }

            if (element.isJsonPrimitive()) {
                JsonPrimitive p = element.getAsJsonPrimitive();
                if (p.isBoolean()) return (V) (Boolean) p.getAsBoolean();
                if (p.isNumber()) return (V) (Double) p.getAsDouble();
                if (p.isString()) return (V) p.getAsString();
            }
        } catch (Exception e) {
            manager.error(IFancyLogging.LogType.ERROR, "Failed to parse map value: {}", e);
        }

        return null;
    }
}