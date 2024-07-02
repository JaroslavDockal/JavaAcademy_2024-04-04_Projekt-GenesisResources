package cz.engeto.ja.genesisResources.service;

import cz.engeto.ja.genesisResources.util.Settings;
import cz.engeto.ja.genesisResources.util.AppLogger;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service class for managing person IDs.
 * This service loads person IDs from a file and provides methods to check and mark them as assigned.
 */
@Service
public class PersonIdService {

    private final Set<String> personIds; // Set to store loaded person IDs
    private ConcurrentMap<String, Boolean> assignedPersonIds; // ConcurrentMap to track assigned person IDs

    /**
     * Constructor initializes the service.
     * Loads person IDs from file and handles any initialization exceptions.
     */
    public PersonIdService() {
        this.personIds = new HashSet<>();
        this.assignedPersonIds = new ConcurrentHashMap<>();
        try {
            loadPersonIdsFromFile();
        } catch (RuntimeException e) {
            AppLogger.warn("Failed to initialize PersonIdService: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves the set of loaded person IDs.
     * @return Set of person IDs
     */
    public Set<String> getPersonIds() {
        return this.personIds;
    }

    /**
     * Checks if a person ID is already assigned to another user.
     * @param personID The person ID to check
     * @return true if the person ID is assigned, false otherwise
     */
    public boolean isPersonIdUsedByOtherUser(String personID) {
        return assignedPersonIds.containsKey(personID);
    }

    /**
     * Marks a person ID as assigned.
     * @param personID The person ID to mark as assigned
     */
    public void markPersonIdAsAssigned(String personID) {
        assignedPersonIds.put(personID, true);
    }

    /**
     * Loads person IDs from a file defined in Settings.PERSON_ID_FILE.
     * Logs loading progress and handles any exceptions that occur during loading.
     */
    private void loadPersonIdsFromFile() {
        AppLogger.info("Start loading person IDs from file: " + Settings.PERSON_ID_FILE);
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(Settings.PERSON_ID_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().matches("[a-zA-Z0-9]{12}")) {
                    AppLogger.info("Loaded personID: " + line);
                    personIds.add(line.trim());
                    count++;
                } else {
                    AppLogger.warn("Skipping invalid personID format: " + line);
                }
            }
            AppLogger.info("Successfully loaded " + count + " valid person IDs from file: " + Settings.PERSON_ID_FILE);
        } catch (IOException e) {
            AppLogger.warn("Failed to load person IDs from file: " + e.getMessage());
            handleFileLoadException(e);
        } catch (RuntimeException e) {
            AppLogger.error("Unexpected runtime exception occurred: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            AppLogger.error("Unexpected exception occurred: " + e.getMessage());
            throw new RuntimeException("Failed to load person IDs from file: " + Settings.PERSON_ID_FILE, e);
        }
    }

    /**
     * Handles exceptions that occur during file loading.
     * Checks file existence, readability, and logs appropriate messages or throws exceptions.
     * @param e The IOException thrown during file loading
     */
    private void handleFileLoadException(IOException e) {
        if (Files.notExists(Path.of(Settings.PERSON_ID_FILE))) {
            AppLogger.warn("File " + Settings.PERSON_ID_FILE + " does not exist. Continuing with an empty list.");
        } else if (!Files.isReadable(Path.of(Settings.PERSON_ID_FILE))) {
            AppLogger.warn("Cannot read from file " + Settings.PERSON_ID_FILE + ".");
            throw new RuntimeException("Cannot read from person ID file: " + Settings.PERSON_ID_FILE);
        } else {
            AppLogger.warn("IO exception occurred: " + e.getMessage());
            throw new RuntimeException("Failed to load person IDs from file: " + Settings.PERSON_ID_FILE, e);
        }
    }
}
