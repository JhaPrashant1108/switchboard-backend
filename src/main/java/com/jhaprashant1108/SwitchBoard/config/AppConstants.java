package com.jhaprashant1108.SwitchBoard.config;

/**
 * Application-wide constants for SwitchBoard feature flag management system.
 * This class centralizes all hardcoded values to improve maintainability and configuration management.
 */
public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ========== RabbitMQ Constants ==========
    public static final class RabbitMQ {
        public static final String EXCHANGE = "app_exchange";
        public static final String ROUTING_KEY = "app.";
        public static final String QUEUE = "app_queue";

        private RabbitMQ() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    // ========== Redis Constants ==========
    public static final class Redis {
        public static final String SWITCH_HASH_KEY = "SWITCH";

        private Redis() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    // ========== API Endpoint Constants ==========
    public static final class Api {
        public static final String SWITCH_BASE_PATH = "/switch";

        public static final class Endpoints {
            public static final String CREATE_SWITCH = "/createswitch";
            public static final String READ_SWITCH = "/readswitch";
            public static final String FETCH_ALL_SWITCHES = "/fetchallswitch";
            public static final String FETCH_SWITCH = "/fetchswitch";
            public static final String UPDATE_SWITCH = "/updateswitch";
            public static final String DELETE_SWITCH = "/deleteswitch";

            private Endpoints() {
                throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
            }
        }

        private Api() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    // ========== CORS Constants ==========
    public static final class Cors {
        public static final String MAPPING_PATH = "/**";

        private Cors() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    // ========== Database Constants ==========
    public static final class Database {
        public static final class SwitchTable {
            public static final String TABLE_NAME = "switch";
            public static final int SWITCH_ID_MAX_LENGTH = 50;
            public static final int USER_FIELD_MAX_LENGTH = 50;
            public static final int TIMESTAMP_FIELD_MAX_LENGTH = 50;

            private SwitchTable() {
                throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
            }
        }

        private Database() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    // ========== Timeout Configuration Constants ==========
    public static final class Timeouts {
        public static final int RABBITMQ_HEARTBEAT_SECONDS = 30;
        public static final int RABBITMQ_CONNECTION_TIMEOUT_MS = 5000;

        private Timeouts() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }
}
