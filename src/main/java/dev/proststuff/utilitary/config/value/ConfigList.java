package dev.proststuff.utilitary.config.value;

import dev.proststuff.utilitary.config.utility.ConfigCodec;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ConfigList<E> extends ConfigValue<List<E>> {
    protected final ConfigCodec<List<E>> codec;

    public ConfigList(Identifier identifier, List<E> list, ConfigCodec<E> elementCodec) {
        super(identifier, new ArrayList<>(list));
        this.codec = elementCodec.asList();
    }

    public ConfigList(Identifier identifier, ConfigCodec<E> codec) {
        this(identifier, List.of(), codec);
    }

    @SafeVarargs
    public ConfigList(Identifier identifier, ConfigCodec<E> codec, E... elements) {
        this(identifier, List.of(elements), codec);
    }

    public void add(E element) {
        value.add(element);
    }

    public void remove(E element) {
        value.remove(element);
    }

    @Override
    public ConfigCodec<List<E>> getCodec() {
        return codec;
    }
}