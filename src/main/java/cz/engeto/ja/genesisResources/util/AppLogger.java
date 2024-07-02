package cz.engeto.ja.genesisResources.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging application messages using SLF4J.
 */
public class AppLogger {
    private static final Logger logger = LoggerFactory.getLogger(AppLogger.class);

    /**
     * Logs a message with INFO level.
     * @param message The message to be logged
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Logs a message with WARN level.
     * @param message The warn message to be logged
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * Logs a message with ERROR level.
     * @param message The error message to be logged
     */
    public static void error(String message) {
        logger.error(message);
    }


}
