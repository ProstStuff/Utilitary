package com.fraynn.utilitary.event;

import java.util.List;
import java.util.function.Function;

// TODO: Add more event types and events

public abstract class Event<T> {
    private final Function<List<T>, T> invokerFactory;
    protected final Object lock = new Object();
    private volatile T invoker;

    protected Event(Function<List<T>, T> invokerFactory) {
        this.invokerFactory = invokerFactory;
        this.invoker = invokerFactory.apply(List.of());
    }

    public static <T> Event<T> create(Function<List<T>, T> invokerFactory) {
        return SimpleEvent.create(invokerFactory);
    }

    public final T invoker() {
        return invoker;
    }

    public abstract void register(T listener);

    protected final void setListeners(List<T> ordered) {
        invoker = invokerFactory.apply(List.copyOf(ordered));
    }
}
