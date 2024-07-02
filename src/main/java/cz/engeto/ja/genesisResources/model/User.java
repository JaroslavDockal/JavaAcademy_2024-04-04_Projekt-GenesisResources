package cz.engeto.ja.genesisResources.model;

import java.util.UUID;

/**
 * A class representing a user.
 */
public class User {

    private Long id;
    private String name;
    private String surname;
    private String personID;
    private final UUID uuid;

    /**
     * Default constructor for creating a new user.
     * Generates a new UUID for each new user.
     */
    public User() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructor for creating a user with specified attributes.
     * @param id The identifier of the user
     * @param name The name of the user
     * @param surname The surname of the user
     * @param personID The personal identifier of the user
     * @param uuidString The UUID of the user as a string
     */
    public User(Long id, String name, String surname, String personID, String uuidString) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personID = personID;
        this.uuid = UUID.fromString(uuidString);
    }

    /**
     * Constructor for creating a user with specified attributes.
     * Generates a new UUID for each new user.
     * @param name The name of the user
     * @param surname The surname of the user
     * @param personID The personal identifier of the user
     */
    public User(String name, String surname, String personID) {
        this.name = name;
        this.surname = surname;
        this.personID = personID;
        this.uuid = UUID.randomUUID();
    }

    /**
     * Sets the identifier of the user.
     * @param id The identifier of the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the name of the user.
     * @param name The name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the surname of the user.
     * @param surname The surname of the user
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Sets the personal identifier of the user.
     * @param personID The personal identifier of the user
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Retrieves the identifier of the user.
     * @return The identifier of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the name of the user.
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the surname of the user.
     * @return The surname of the user
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Retrieves the personal identifier of the user.
     * @return The personal identifier of the user
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Retrieves the UUID of the user.
     * @return The UUID of the user
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Converts the user object to a string representation.
     * @return A string representation of the user object
     */
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
