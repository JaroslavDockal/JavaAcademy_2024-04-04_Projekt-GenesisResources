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

@Service
public class PersonIdService {

    private final Set<String> personIds;
    private ConcurrentMap<String, Boolean> assignedPersonIds;

    public PersonIdService() {
        this.personIds = new HashSet<>();
        this.assignedPersonIds = new ConcurrentHashMap<>();
        try {
            loadPersonIdsFromFile();
        } catch (RuntimeException e) {
            AppLogger.log("Failed to initialize PersonIdService: " + e.getMessage());
            throw e;
        }
    }

    public Set<String> getPersonIds() {
        return this.personIds;
    }

    public boolean isPersonIdUsedByOtherUser(String personID) {
        return assignedPersonIds.containsKey(personID);
    }

    public void markPersonIdAsAssigned(String personID) {
        assignedPersonIds.put(personID, true);
    }

    private void loadPersonIdsFromFile() {
        AppLogger.log("Start loading person IDs from file: " + Settings.PERSON_ID_FILE);
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(Settings.PERSON_ID_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().matches("[a-zA-Z0-9]{12}")) {
                    AppLogger.log("Loaded personID: " + line);
                    personIds.add(line.trim());
                    count++;
                } else {
                    AppLogger.log("Skipping invalid personID format: " + line);
                }
            }
            AppLogger.log("Successfully loaded " + count + " valid person IDs from file: " + Settings.PERSON_ID_FILE);
        } catch (IOException e) {
            AppLogger.log("Failed to load person IDs from file: " + e.getMessage());
            handleFileLoadException(e);
        } catch (RuntimeException e) {
            AppLogger.log("Unexpected runtime exception occurred: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            AppLogger.log("Unexpected exception occurred: " + e.getMessage());
            throw new RuntimeException("Failed to load person IDs from file: " + Settings.PERSON_ID_FILE, e);
        }
    }

    private void handleFileLoadException(IOException e) {
        if (Files.notExists(Path.of(Settings.PERSON_ID_FILE))) {
            AppLogger.log("File " + Settings.PERSON_ID_FILE + " does not exist. Continuing with an empty list.");
        } else if (!Files.isReadable(Path.of(Settings.PERSON_ID_FILE))) {
            AppLogger.log("Cannot read from file " + Settings.PERSON_ID_FILE + ".");
            throw new RuntimeException("Cannot read from person ID file: " + Settings.PERSON_ID_FILE);
        } else {
            AppLogger.log("IO exception occurred: " + e.getMessage());
            throw new RuntimeException("Failed to load person IDs from file: " + Settings.PERSON_ID_FILE, e);
        }
    }
}
