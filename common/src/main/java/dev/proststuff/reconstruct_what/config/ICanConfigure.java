package dev.proststuff.reconstruct_what.config;

import com.google.gson.JsonElement;

public interface ICanConfigure<T> {

    String getName();
    T get();

    default void serializing(ConfigManager configManager) {}
    default void serialized(ConfigManager configManager) {}
    default void deserializing(ConfigManager configManager) {}
    default void deserialized(ConfigManager configManager) {}

    JsonElement serialize(ConfigManager manager);
    void deserialize(JsonElement element, ConfigManager manager);

    default boolean is(String name) {
        return this.getName().equals(name);
    }

    default boolean is(Object obj) {
        return this.get().equals(obj);
    }
}
