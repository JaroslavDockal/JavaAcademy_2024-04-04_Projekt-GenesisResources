package cz.engeto.ja.genesisResources.model;

import java.util.UUID;

public class User {

    private Long id;
    private String name;
    private String surname;
    private String personID;
    private final UUID uuid;

    public User() {
        this.uuid = UUID.randomUUID();
    }

    public User(Long id, String name, String surname, String personID, String uuidString) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personID = personID;
        this.uuid = UUID.fromString(uuidString);
    }

    public User(String name, String surname, String personID) {
        this.name = name;
        this.surname = surname;
        this.personID = personID;
        this.uuid = UUID.randomUUID();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPersonID() {
        return personID;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", personID='" + personID + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
