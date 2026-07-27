package dev.proststuff.utilitary.api.v1.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public interface Either<L, R> {
    @NonNull Either<L, R> ifLeft(Consumer<L> consumer);
    @NonNull Either<L, R> ifRight(Consumer<R> consumer);
    @NonNull Optional<L> left();
    @NonNull Optional<R> right();

    default boolean isLeft() {
        return left().isPresent();
    }

    default boolean isRight() {
        return right().isPresent();
    }

    default boolean isEmpty() {
        return left().isEmpty() && right().isEmpty();
    }

    default @Nullable L getLeft() {
        return left().orElse(null);
    }

    default L getLeftOrThrow() {
        return left().orElseThrow();
    }

    default @Nullable R getRight() {
        return right().orElse(null);
    }

    default R getRightOrThrow() {
        return right().orElseThrow();
    }

    class Left<L, R> implements Either<L, R> {
        protected @NonNull L value;

        public Left(@NonNull L value) {
            this.value = value;
        }

        @Override
        public @NonNull Either<L, R> ifLeft(Consumer<L> consumer) {
            consumer.accept(value);
            return this;
        }

        @Override
        public @NonNull Either<L, R> ifRight(Consumer<R> consumer) {
            return this;
        }

        @Override
        public @NonNull Optional<L> left() {
            return Optional.of(value);
        }

        @Override
        public @NonNull Optional<R> right() {
            return Optional.empty();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    class Right<L, R> implements Either<L, R> {
        protected @NonNull R value;

        public Right(@NonNull R value) {
            this.value = value;
        }

        @Override
        public @NonNull Either<L, R> ifLeft(Consumer<L> consumer) {
            return this;
        }

        @Override
        public @NonNull Either<L, R> ifRight(Consumer<R> consumer) {
            consumer.accept(value);
            return this;
        }

        @Override
        public @NonNull Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public @NonNull Optional<R> right() {
            return Optional.of(value);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    class Empty<L, R> implements Either<L, R> {
        public static final Empty<?, ?> EMPTY = new Empty<>();

        private Empty() {}

        @Override
        public @NonNull Either<L, R> ifLeft(Consumer<L> consumer) {
            return this;
        }

        @Override
        public @NonNull Either<L, R> ifRight(Consumer<R> consumer) {
            return this;
        }

        @Override
        public @NonNull Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public @NonNull Optional<R> right() {
            return Optional.empty();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }

    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> empty() {
        return (Either<L, R>) Empty.EMPTY;
    }
}
