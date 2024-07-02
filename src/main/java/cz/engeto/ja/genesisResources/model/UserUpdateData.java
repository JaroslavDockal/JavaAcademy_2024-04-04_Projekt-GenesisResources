package cz.engeto.ja.genesisResources.model;

/**
 * Represents the data required to update a user.
 */
public class UserUpdateData {
    private String id;
    private String name;
    private String surname;

    /**
     * Retrieves the ID of the user.
     * @return The ID of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     * @param id The ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the name of the user.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the user.
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the surname of the user.
     * @param surname The surname to set.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Retrieves the surname of the user.
     * @return The surname of the user.
     */
    public String getSurname() {
        return surname;
    }
}
