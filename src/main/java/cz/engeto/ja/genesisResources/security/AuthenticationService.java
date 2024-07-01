package cz.engeto.ja.genesisResources.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    private static final Map<String, UserEntity> users = new HashMap<>();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static {
        String operatorPassword = passwordEncoder.encode("password");
        String adminPassword = passwordEncoder.encode("admin");

        users.put("operator", new UserEntity("operator", operatorPassword, Role.OPERATOR));
        users.put("admin", new UserEntity("admin", adminPassword, Role.ADMIN));
    }

    public static boolean authenticate(String username, String password) {
        UserEntity user = getUser(username);
        return user != null && passwordEncoder.matches(password, user.getPasswordHash());
    }

    public static boolean hasAdminRole(String username) {
        UserEntity user = getUser(username);
        return user != null && user.getRole() == Role.ADMIN;
    }

    public static UserEntity getUser(String username) {
        return users.get(username);
    }
}
