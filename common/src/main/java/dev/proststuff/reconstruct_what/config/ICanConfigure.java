package dev.proststuff.reconstruct_what.config;

import com.google.gson.JsonElement;

public interface ICanConfigure<T> {

    String getName();
    T get();

    default void onLoad(ConfigManager configManager){} // Called when value is first loaded.
    default void onChange(){} // Called when a value is modified.
    default void preSave(ConfigManager configManager){} // Value can be modified before saving.
    default void postSave(ConfigManager configManager){} // Value is already modified.

    JsonElement serialize(ConfigManager manager);
    void deserialize(JsonElement element, ConfigManager manager);

    default boolean isFor(String name) {
        return this.getName().equals(name);
    }

    default boolean isFor(Object object) {
        if (object instanceof ICanConfigure<?> configuration) {
            return this.isFor(configuration.getName());
        }

        return false;
    }
}
