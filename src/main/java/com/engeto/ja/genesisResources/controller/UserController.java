package com.engeto.ja.genesisResources.controller;

import com.engeto.ja.genesisResources.model.User;
import com.engeto.ja.genesisResources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user.getName(), user.getSurname(), user.getPersonID());
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/user/{uuid}")
    public ResponseEntity<User> getUser(@PathVariable UUID uuid) {
        User user = userService.getUserByUuid(uuid);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user.getUuid(), user.getName(), user.getSurname());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
        return ResponseEntity.noContent().build();
    }
}
