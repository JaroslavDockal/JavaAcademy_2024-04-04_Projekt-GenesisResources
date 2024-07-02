package cz.engeto.ja.genesisResources.model;

/**
 * A class representing basic information about a user.
 */
public class UserBasicInfo {

    private Long id;
    private String name;
    private String surname;

    /**
     * Constructs a new UserBasicInfo object with specified id, name, and surname.
     * @param id The unique identifier of the user
     * @param name The name of the user
     * @param surname The surname of the user
     */
    public UserBasicInfo(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Retrieves the unique identifier of the user.
     * @return The user's id
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the name of the user.
     * @return The user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the surname of the user.
     * @return The user's surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Creates a new UserBasicInfo object from a given User object.
     * @param user The User object from which to create UserBasicInfo
     * @return UserBasicInfo object containing basic information of the user
     */
    public static UserBasicInfo fromUser(User user) {
        return new UserBasicInfo(user.getId(), user.getName(), user.getSurname());
    }

    /**
     * Returns a string representation of the UserBasicInfo object.
     * @return A string representation of the UserBasicInfo object
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}