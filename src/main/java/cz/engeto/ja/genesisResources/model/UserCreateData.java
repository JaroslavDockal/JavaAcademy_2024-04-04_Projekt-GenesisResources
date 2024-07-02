package cz.engeto.ja.genesisResources.model;

/**
 * Represents the data required to create a user.
 */
public class UserCreateData {
    private String name;
    private String surname;
    private String personID;
    /**
     * Retrieves the person ID of the user.
     * @return The ID of the user.
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the person ID of the user.
     * @param personId The ID to set.
     */
    public void setPersonID(String personId) {
        this.personID = personId;
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
