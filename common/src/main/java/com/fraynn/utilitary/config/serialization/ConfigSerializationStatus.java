package com.fraynn.utilitary.config.serialization;

import com.fraynn.utilitary.util.Tried;

import java.util.Optional;

public interface ConfigSerializationStatus extends Tried<String> {
    static ConfigSerializationStatus succeed(String message) {
        return new Success(message);
    }

    static ConfigSerializationStatus fail(String message) {
        return new Failure(new Exception(message), message);
    }

    static ConfigSerializationStatus fail(Exception exception) {
        return new Failure(exception, exception.toString());
    }

    String getAsString();

    record Success(String message) implements ConfigSerializationStatus {

        @Override
        public String getAsString() {
            return message;
        }

        @Override
        public Optional<String> get() {
            return Optional.of(message);
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

    record Failure(Exception exception, String message) implements ConfigSerializationStatus {
        @Override
        public String getAsString() {
            return message;
        }

        @Override
        public Optional<String> get() {
            return Optional.of(message);
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
