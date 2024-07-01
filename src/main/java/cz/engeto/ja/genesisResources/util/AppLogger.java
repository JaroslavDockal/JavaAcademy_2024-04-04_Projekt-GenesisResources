package cz.engeto.ja.genesisResources.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {
    private static final Logger logger = LoggerFactory.getLogger(AppLogger.class);

    public static void log(String message) {
        logger.info(message);
    }
}
