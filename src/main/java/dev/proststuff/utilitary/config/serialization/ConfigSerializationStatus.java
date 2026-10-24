package dev.proststuff.utilitary.config.serialization;

import dev.proststuff.utilitary.util.Accomplishable;

public interface ConfigSerializationStatus extends Accomplishable {
    static ConfigSerializationStatus succeed(String message) {
        return new Success(message);
    }

    static ConfigSerializationStatus fail(String message) {
        return new Failure(message);
    }

    static ConfigSerializationStatus fail(Exception exception) {
        return new Failure(exception.toString());
    }

    String getAsString();

    record Success(String message) implements ConfigSerializationStatus {

        @Override
        public String getAsString() {
            return message;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }
    }

    record Failure(String message) implements ConfigSerializationStatus {
        @Override
        public String getAsString() {
            return message;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }
    }
}
