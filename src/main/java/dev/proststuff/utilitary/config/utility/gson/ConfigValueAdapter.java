package dev.proststuff.utilitary.config.utility.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.proststuff.utilitary.config.value.ConfigValue;

import java.io.IOException;

public class ConfigValueAdapter<T> extends TypeAdapter<ConfigValue<T>> {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ConfigValue.class, new ConfigValueAdapter<>())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setLenient()
            .serializeNulls()
            .create();

    @Override
    public void write(JsonWriter out, ConfigValue<T> value) throws IOException {
        GSON.toJson(value.get(), value.get().getClass(), out);
    }

    @Deprecated
    @Override
    public ConfigValue<T> read(JsonReader in) throws IOException {
        return null;
    }
}