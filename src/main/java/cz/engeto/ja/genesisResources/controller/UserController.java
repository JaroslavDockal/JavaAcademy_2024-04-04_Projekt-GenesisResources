package cz.engeto.ja.genesisResources.controller;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) throws SQLException {
        userService.createUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) throws SQLException {
        return userService.getUserById(id);
    }

    @GetMapping("/uuid/{uuid}")
    public User getUserByUuid(@PathVariable UUID uuid) throws SQLException {
        return userService.getUserByUuid(uuid);
    }

    @GetMapping
    public List<User> getAllUsers() throws SQLException {
        return userService.getAllUsers();
    }

    @PutMapping("/{uuid}")
    public User updateUser(@PathVariable UUID uuid, @RequestBody User user) throws SQLException {
        user.setUuid(uuid);
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("/{uuid}")
    public void deleteUser(@PathVariable UUID uuid) throws SQLException {
        userService.deleteUser(uuid);
    }
}
