package dev.proststuff.utilitary.utility.config.gson;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.proststuff.utilitary.config.ConfigFile;
import dev.proststuff.utilitary.config.ConfigValue;

import java.io.IOException;

/**
 * Convert ConfigValue into a more readable form.
 * Default adapter turn ConfigValue into </br>
 * <code>
 * "someConfig": {"value": "someValue"}
 * </code></br>
 * this adapter will turn it into</br>
 * <code>
 * "someConfig": "someValue"
 * </code>
 */
public class ConfigValueAdapter<T> extends TypeAdapter<ConfigValue<T>> {
    @Override
    public void write(JsonWriter out, ConfigValue<T> value) throws IOException {
        ConfigFile.GSON.toJson(value.get(), value.get().getClass(), out);
    }

    /**
     * @deprecated ConfigManager handles deserialization.
     * @see dev.proststuff.utilitary.config.ConfigManager
     */
    @Deprecated
    @Override
    public ConfigValue<T> read(JsonReader in) throws IOException {
        return null;
    }
}