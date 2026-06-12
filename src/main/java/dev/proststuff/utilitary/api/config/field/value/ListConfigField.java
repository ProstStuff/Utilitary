package dev.proststuff.utilitary.api.config.field.value;

import com.google.common.collect.ImmutableList;
import dev.proststuff.utilitary.api.config.codec.ConfigCodec;
import dev.proststuff.utilitary.api.config.field.ConfigField;

import java.util.ArrayList;
import java.util.List;

public class ListConfigField<V> extends ConfigField<List<V>> {
    public ListConfigField(String name, List<V> values, ConfigCodec<V> valueCodec, boolean immutable) {
        super(name, ImmutableList.copyOf(values), ConfigCodec.listOf(valueCodec, immutable));
        this.value = new ArrayList<>();
        this.value.addAll(values);
    }

    public ListConfigField(String name, List<V> values, ConfigCodec<V> valueCodec) {
        super(name, ImmutableList.copyOf(values), ConfigCodec.listOf(valueCodec, false));
    }

    public boolean add(V value) {
        return get().add(value);
    }

    public void add(int index, V value) {
        get().add(index, value);
    }

    public V get(int index) {
        return get().get(index);
    }

    public V remove(int index) {
        return get().remove(index);
    }

    public void clear() {
        get().clear();
    }

    @Override
    public List<V> validate(List<V> value) {
        return super.validate(value);
    }
}
