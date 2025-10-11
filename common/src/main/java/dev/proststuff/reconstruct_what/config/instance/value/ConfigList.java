package dev.proststuff.reconstruct_what.config.instance.value;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.ConfigCodec;
import dev.proststuff.reconstruct_what.config.instance.ConfigValue;

import java.util.ArrayList;
import java.util.List;

public class ConfigList<E> extends ConfigValue<List<E>> {
    protected final ConfigCodec<E> elementCodec;

    public ConfigList(String name, List<E> defaultValue, ConfigCodec<E> codec, boolean runtimeOnly) {
        super(name, new ArrayList<>(defaultValue), ConfigCodec.list(defaultValue), runtimeOnly);
        this.elementCodec = codec;
    }

    public ConfigList(String name, List<E> defaultValue, ConfigCodec<E> codec) {
        this(name, new ArrayList<>(defaultValue), codec, false);
    }

    public ConfigList(String name, ConfigCodec<E> codec, boolean runtimeOnly) {
        this(name, List.of(), codec, runtimeOnly);
    }

    public ConfigList(String name, ConfigCodec<E> codec) {
        this(name, codec, false);
    }

    @SafeVarargs
    public ConfigList(String name, ConfigCodec<E> codec, boolean runtimeOnly, E... elements) {
        this(name, List.of(elements), codec, runtimeOnly);
    }

    @SafeVarargs
    public ConfigList(String name, ConfigCodec<E> codec, E... elements) {
        this(name, codec, false, elements);
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        JsonArray array = new JsonArray();
        for (E element : value) {
            array.add(elementCodec.encode(element));
        }
        return array;
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        if (!element.isJsonArray()) return;
        JsonArray array = element.getAsJsonArray();

        List<E> list = new ArrayList<>();
        for (JsonElement json : array) {
            list.add(elementCodec.decode(json));
        }

        this.value = list;
        this.setLoaded();
    }

    public void add(E element) {
        value.add(element);
        changed();
    }

    public void remove(E element) {
        value.remove(element);
        changed();
    }
}