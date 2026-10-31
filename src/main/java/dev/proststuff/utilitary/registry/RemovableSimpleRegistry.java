package dev.proststuff.utilitary.registry;

public class RemovableSimpleRegistry<K, V> extends SimpleRegistry<K, V> {
    public RemovableSimpleRegistry(boolean strict) {
        super(strict);
    }

    public RemovableSimpleRegistry() {
        this(false);
    }

    @Override
    public V register(K key, V value) {
        if (keys.contains(key)) unregister(key);
        return super.register(key, value);
    }

    public V unregister(K key) {
        int i = keys.indexOf(key);
        keys.remove(i);
        entries.remove(i);
        return values.remove(i);
    }

    public K unregisterValue(V value) {
        int i = values.indexOf(value);
        values.remove(i);
        entries.remove(i);
        return keys.remove(i);
    }

    public void unregisterAll() {
        keys.clear();
        values.clear();
        entries.clear();
    }
}
