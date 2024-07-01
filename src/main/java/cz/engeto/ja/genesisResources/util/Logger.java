package cz.engeto.ja.genesisResources.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Settings.LOG_TIME_FORMAT);

    public static void log(String message) {
        String timeStampedMessage = DATE_FORMATTER.format(LocalDateTime.now()) + " - " + message;

        if (Settings.CONSOLE_OUTPUT_ENABLED) {
            System.out.println(timeStampedMessage);
        }

        if (Settings.LOG_ENABLED) {
            try (FileWriter fileWriter = new FileWriter(Settings.LOG_FILE, true)) {
                fileWriter.write(timeStampedMessage + "\n");
            } catch (IOException e) {
                System.err.println("Failed to write to log file: " + e.getMessage());
            }
        }
    }
}
