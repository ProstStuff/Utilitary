package dev.proststuff.utilitary.registry;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleRegistry<K, V> {
    protected final List<V> values = new ArrayList<>();
    protected final List<K> keys = new ArrayList<>();
    protected final List<Entry<K, V>> entries = new ArrayList<>();

    protected final boolean strict;

    public SimpleRegistry(boolean strict) {
        this.strict = strict;
    }

    public SimpleRegistry() {
        this(false);
    }

    public List<V> values() {
        return values;
    }

    public List<K> keys() {
        return keys;
    }

    public List<Entry<K, V>> entries() {
        return entries;
    }

    public @Nullable V get(K key) {
        int i = keys.indexOf(key);
        return i >= 0 && i < values.size() ? values.get(i) : null;
    }

    public @Nullable K getKey(V value) {
        int i = values.indexOf(value);
        return i >= 0 && i < values.size() ? keys.get(i) : null;
    }

    public V register(K key, V value) {
        if (strict && (keys.contains(key) || values.contains(value))) throw new IllegalStateException("Cannot have duplicate keys or value");
        values.add(value);
        keys.add(key);
        entries.add(new Entry<>(key, value));
        if (values.size() != keys.size() && keys.size() != entries().size()) throw new IllegalStateException("Unbalanced registry entry size! There are " + keys.size() + " keys," + values.size() + " values, and " + entries.size() + " entries");
        return value;
    }

    public record Entry<K, V>(K key, V value) {}
}
