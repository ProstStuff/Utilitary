package com.fraynn.utilitary.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SimpleEvent<T> extends Event<T> {
    private List<T> listeners = List.of();

    SimpleEvent(Function<List<T>, T> factory) {
        super(factory);
    }

    @Override
    public void register(T listener) {
        Objects.requireNonNull(listener, "no listener");

        synchronized (lock) {
            var copy = new ArrayList<>(listeners);
            copy.add(listener);
            listeners = copy;
            setListeners(copy);
        }
    }

    public static <T> Event<T> create(Function<List<T>, T> invokerFactory) {
        return new SimpleEvent<>(invokerFactory);
    }
}