package adapters;

public class LoggerAdapter {
    public static void info(Object logger, String message) {
        if (logger instanceof org.slf4j.Logger) {
            org.slf4j.Logger castedLogger = (org.slf4j.Logger) logger;
            castedLogger.info(message);
        } else if (logger instanceof java.util.logging.Logger) {
            java.util.logging.Logger castedLogger = (java.util.logging.Logger) logger;
            castedLogger.info(message);
        }
    }

    public static void warn(Object logger, String message) {
        if (logger instanceof org.slf4j.Logger) {
            org.slf4j.Logger castedLogger = (org.slf4j.Logger) logger;
            castedLogger.warn(message);
        } else if (logger instanceof java.util.logging.Logger) {
            java.util.logging.Logger castedLogger = (java.util.logging.Logger) logger;
            castedLogger.warning(message);
        }
    }

    public static void error(Object logger, String message) {
        if (logger instanceof org.slf4j.Logger) {
            org.slf4j.Logger castedLogger = (org.slf4j.Logger) logger;
            castedLogger.error(message);
        } else if (logger instanceof java.util.logging.Logger) {
            java.util.logging.Logger castedLogger = (java.util.logging.Logger) logger;
            castedLogger.severe(message);
        }
    }
}
