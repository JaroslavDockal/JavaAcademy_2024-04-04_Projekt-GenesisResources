package cz.engeto.ja.genesisResources.security;

import java.util.HashMap;
import java.util.Map;

public class UserAccounts {

    private static final Map<String, UserEntity> users = new HashMap<>();

    static {
        users.put("operator", new UserEntity("operator", "password", Role.OPERATOR));
        users.put("admin", new UserEntity("admin", "admin", Role.ADMIN));
    }

    public static UserEntity getUser(String username) {
        return users.get(username);
    }
}