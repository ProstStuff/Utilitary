package dev.proststuff.reconstruct_what.config;

import com.google.gson.JsonElement;

public interface ICanConfigure<T> {

    String getName();
    T get();

    default void loaded(ConfigManager configManager){} // Called when value is loaded/reloaded.
    default void changed(){} // Called when value is modified.
    default void preSave(ConfigManager configManager){} // Value can be modified before saving.
    default void postSave(ConfigManager configManager){} // Value is already modified.

    JsonElement serialize(ConfigManager manager);
    void deserialize(JsonElement element, ConfigManager manager);

    default boolean is(String name) {
        return this.getName().equals(name);
    }

    default boolean is(Object obj) {
        return this.get().equals(obj);
    }
}
