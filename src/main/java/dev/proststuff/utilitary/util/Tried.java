package dev.proststuff.utilitary.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Tried<T> extends Accomplishable {
    // T Can be null
    Optional<T> get();
    Optional<Exception> getException();

    default @Nullable T value() {
        return get().orElse(null);
    }

    default T getOrThrow() {
        return get().orElseThrow();
    }

    default Tried<T> ifSuccessful(Consumer<T> consumer) {
        get().ifPresent(consumer);
        return this;
    }
    default Tried<T> ifFailure(Consumer<Exception> consumer) {
        getException().ifPresent(consumer);
        return this;
    }

    static <T> Tried<T> succeed(T value) {
        return new Success<>(value);
    }

    static <T> Tried<T> fail(Exception exception) {
        return new Failure<>(exception);
    }

    static <T> Tried<T> tryCatch(Supplier<T> runner) {
        try {
            return succeed(runner.get());
        } catch (Exception e) {
            return fail(e);
        }
    }

    class Success<T> implements Tried<T> {
        protected @Nullable T value;

        public Success(@Nullable T value) {
            this.value = value;
        }

        @Override
        public Optional<T> get() {
            return Optional.ofNullable(value);
        }

        @Override
        public Optional<Exception> getException() {
            return Optional.empty();
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }
    }

    class Failure<T> implements Tried<T> {
        protected @NonNull Exception exception;

        public Failure(@NonNull Exception exception) {
            this.exception = exception;
        }

        @Override
        public Optional<T> get() {
            return Optional.empty();
        }

        @Override
        public Optional<Exception> getException() {
            return Optional.of(exception);
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }
    }
}