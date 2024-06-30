package cz.engeto.ja.genesisResources.model;

public class UserBasicInfo {

    private Long id;
    private String name;
    private String surname;

    public UserBasicInfo(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
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

    public static UserBasicInfo fromUser(User user) {
        return new UserBasicInfo(user.getId(), user.getName(), user.getSurname());
    }
}
