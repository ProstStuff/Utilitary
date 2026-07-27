package dev.proststuff.utilitary.api.v1.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Group<K, V> {
    protected final Supplier<Map<K, Either<V, Group<K, V>>>> mapBuilder;
    protected final @Nullable Group<K, V> parent;
    protected final Map<K, Either<V, Group<K, V>>> children;

    public Group(@Nullable Group<K, V> parent, Supplier<Map<K, Either<V, Group<K, V>>>> mapBuilder) {
        this.parent = parent;
        this.mapBuilder = mapBuilder;
        this.children = mapBuilder.get();
    }

    public Group(@NonNull Group<K, V> parent) {
        this(parent, HashMap::new);
    }

    public Group(Supplier<Map<K, Either<V, Group<K, V>>>> mapBuilder) {
        this(null, mapBuilder);
    }

    public Group() {
        this(null, HashMap::new);
    }

    public void visitAll(Consumer<V> visitor) {
        recurse(this, visitor);
    }

    public void visit(Consumer<V> visitor) {
        for (Either<V, Group<K, V>> value : children.values()) {
            value.ifLeft(visitor);
        }
    }

    public @Nullable Group<K, V> getParent() {
        return parent;
    }

    public Either<V, Group<K, V>> get(K key) {
        return children.getOrDefault(key, Either.empty());
    }

    public Either<V, Group<K, V>> getOrCreate(K key) {
        return children.computeIfAbsent(key, (_) -> Either.right(new Group<>(this, this.mapBuilder)));
    }

    public @Nullable V getValue(K key) {
        return get(key).getLeft();
    }

    public @Nullable Group<K, V> getRight(K key) {
        return get(key).getRight();
    }

    public Map<K, Either<V, Group<K, V>>> getChildren() {
        return children;
    }

    public void set(K key, V value) {
        children.put(key, Either.left(value));
    }

    public void set(K key, Group<K, V> group) {
        children.put(key, Either.right(group));
    }

    private static <K, V> void recurse(Group<K, V> group, Consumer<V> valueVisitor) {
        for (Either<V, Group<K, V>> value : group.children.values()) {
            value.ifLeft(valueVisitor);
            value.ifRight((g) -> recurse(g, valueVisitor));
        }
    }
}