package cz.engeto.ja.genesisResources.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserEntity {

    private final String username;
    private String passwordHash;
    private final Role role;

    public UserEntity(String username, String passwordHash, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }
}
