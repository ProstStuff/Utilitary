package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.util.Tried;

import java.util.Optional;
import java.util.ServiceLoader;

public interface ServiceUtil {
    static <T> Optional<T> loadOptional(Class<T> clazz) {
        return ServiceLoader.load(clazz, ServiceUtil.class.getClassLoader()).findFirst();
    }

    static <T> T load(Class<T> clazz) {
        return loadOptional(clazz).orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    static <T> Tried<T> tryLoad(Class<T> clazz) {
        return Tried.tryCatch(() -> load(clazz));
    }
}