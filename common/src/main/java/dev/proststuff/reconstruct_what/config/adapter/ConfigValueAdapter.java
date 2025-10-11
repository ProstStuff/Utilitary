package dev.proststuff.reconstruct_what.config.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.io.IOException;

import static dev.proststuff.reconstruct_what.config.ConfigHelper.GSON;

/**
 * Config value adapter to turn
 * <code>
 * "someConfig": {"value": "someValue"}
 * </code>
 * into
 * <code>
 * "someConfig": "someValue"
 * </code> for more readability.
 * @param <T>
 */
public class ConfigValueAdapter<T> extends TypeAdapter<ConfigValue<T>> {
    @Override
    public void write(JsonWriter out, ConfigValue<T> value) throws IOException {
        GSON.toJson(value.get(), value.get().getClass(), out);
    }

    /**
     * @deprecated Unused read methods. Do not touch or use.
     */
    @Deprecated
    @Override
    public ConfigValue<T> read(JsonReader in) throws IOException {
        return null;
    }
}
