package dev.proststuff.utilitary.config.template;

import dev.proststuff.utilitary.config.ConfigValue;
import dev.proststuff.utilitary.config.ConfigCodec;

import java.util.ArrayList;
import java.util.List;

public class ConfigList<E> extends ConfigValue<List<E>> {
    protected final ConfigCodec<List<E>> codec;

    public ConfigList(String name, List<E> list, ConfigCodec<E> elementCodec) {
        super(name, new ArrayList<>(list));
        this.codec = elementCodec.asList();
    }

    public ConfigList(String name, ConfigCodec<E> codec) {
        this(name, List.of(), codec);
    }

    @SafeVarargs
    public ConfigList(String name, ConfigCodec<E> codec, E... elements) {
        this(name, List.of(elements), codec);
    }

    public void add(E element) {
        value.add(element);
        changed();
    }

    public void remove(E element) {
        value.remove(element);
        changed();
    }

    @Override
    public ConfigCodec<List<E>> getCodec() {
        return codec;
    }
}