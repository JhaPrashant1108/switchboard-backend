package com.jhaprashant1108.SwitchBoard.config;

/**
 * Centralized error messages for the SwitchBoard application.
 * This class contains all error messages to ensure consistency and ease of maintenance.
 */
public final class ErrorMessages {

    private ErrorMessages() {
        // Prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ========== Connection Error Messages ==========
    public static final class Connection {
        public static final String REDIS_CONNECTION_FAILED = "Cannot connect to Redis at startup";
        public static final String RABBITMQ_CONNECTION_FAILED = "Failed to connect to RabbitMQ at startup";

        private Connection() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    // ========== Serialization Error Messages ==========
    public static final class Serialization {
        public static final String SERIALIZATION_ERROR = "Error serializing data map";
        public static final String DESERIALIZATION_ERROR = "Error deserializing data map";

        private Serialization() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }
}
