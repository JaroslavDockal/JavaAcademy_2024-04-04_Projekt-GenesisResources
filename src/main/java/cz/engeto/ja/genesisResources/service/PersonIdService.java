package cz.engeto.ja.genesisResources.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class PersonIdService {

    private Set<String> personIds;
    private ConcurrentMap<String, Boolean> assignedPersonIds;

    public PersonIdService() {
        this.personIds = loadPersonIdsFromFile("dataPersonId.txt");
        this.assignedPersonIds = new ConcurrentHashMap<>();
    }

    public Set<String> getPersonIds() {
        return this.personIds;
    }

    public Set<String> getAssignedPersonIds() {
        return new HashSet<>(assignedPersonIds.keySet());
    }

    public String assignPersonId() {
        for (String id : personIds) {
            if (assignedPersonIds.putIfAbsent(id, true) == null) {
                System.out.println("Assigned personID: " + id);
                return id;
            }
        }
        System.out.println("No available personID found.");
        return null; // All personIDs are already assigned
    }

    public boolean isPersonIdAlreadyAssigned(String personID) {
        return assignedPersonIds.containsKey(personID);
    }

    private Set<String> loadPersonIdsFromFile(String fileName) {
        Set<String> ids = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Loaded personID: " + line);
                ids.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }
}
