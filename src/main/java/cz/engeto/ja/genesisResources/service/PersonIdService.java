package cz.engeto.ja.genesisResources.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonIdService {

    private List<String> personIds;

    public PersonIdService() {
        this.personIds = loadPersonIdsFromFile("dataPersonId.txt");
    }

    public List<String> getPersonIds() {
        return personIds;
    }

    private List<String> loadPersonIdsFromFile(String fileName) {
        List<String> ids = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                ids.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }
}
