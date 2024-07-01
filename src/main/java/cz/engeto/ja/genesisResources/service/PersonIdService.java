package cz.engeto.ja.genesisResources.service;

import cz.engeto.ja.genesisResources.util.Settings;
import cz.engeto.ja.genesisResources.util.Logger;
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

    private Set<String> personIds;
    private ConcurrentMap<String, Boolean> assignedPersonIds;

    public PersonIdService() {
        this.personIds = new HashSet<>(); // Initialize with an empty set initially
        this.assignedPersonIds = new ConcurrentHashMap<>();
        try {
            loadPersonIdsFromFile(Settings.PERSON_ID_FILE);
        } catch (RuntimeException e) {
            // Log the exception or handle it appropriately
            Logger.log("Failed to initialize PersonIdService: " + e.getMessage());
            // Propagate the exception further if necessary
            throw e;
        }
    }

    public Set<String> getPersonIds() {
        return this.personIds;
    }

    public Set<String> getAssignedPersonIds() {
        return new HashSet<>(assignedPersonIds.keySet());
    }

    public boolean isPersonIdUsedByOtherUser(String personID) {
        return assignedPersonIds.containsKey(personID);
    }

    public void markPersonIdAsAssigned(String personID) {
        assignedPersonIds.put(personID, true);
    }

    private void loadPersonIdsFromFile(String fileName) {
        Logger.log("Start loading person IDs from file: " + fileName);
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                Logger.log("Loaded personID: " + line);
                personIds.add(line.trim());
                count++;
            }
            Logger.log("Successfully loaded " + count + " person IDs from file: " + fileName);
        } catch (IOException e) {
            Logger.log("Failed to load person IDs from file: " + e.getMessage());
            handleFileLoadException(fileName, e);
        } catch (RuntimeException e) {
            Logger.log("Unexpected runtime exception occurred: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            Logger.log("Unexpected exception occurred: " + e.getMessage());
            throw new RuntimeException("Failed to load person IDs from file: " + fileName, e);
        }
    }

    private void handleFileLoadException(String fileName, IOException e) {
        // Handle specific exceptions
        if (Files.notExists(Path.of(fileName))) {
            Logger.log("File " + fileName + " does not exist. Continuing with an empty list.");
            // Continue with empty personIds set
        } else if (!Files.isReadable(Path.of(fileName))) {
            Logger.log("Cannot read from file " + fileName + ".");
            throw new RuntimeException("Cannot read from person ID file: " + fileName);
        } else {
            // Handle other IO exceptions
            Logger.log("IO exception occurred: " + e.getMessage());
            throw new RuntimeException("Failed to load person IDs from file: " + fileName, e);
        }
    }
}
