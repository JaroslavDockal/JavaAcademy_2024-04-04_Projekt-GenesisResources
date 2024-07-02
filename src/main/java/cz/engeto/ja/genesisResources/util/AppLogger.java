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
     *
     * @param message The message to be logged
     */
    public static void log(String message) {
        logger.info(message);
    }
}
