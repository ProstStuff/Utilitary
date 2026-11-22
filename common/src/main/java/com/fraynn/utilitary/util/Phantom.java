package com.fraynn.utilitary.util;

public interface Phantom<T> {
    T get();

    @SuppressWarnings({"rawtypes", "unchecked"})
    static <T> Phantom<T> simple(T object) {
        return new Simple(object);
    }

    record Simple<T>(T get) implements Phantom<Object> {}
}