package cz.engeto.ja.genesisResources.controller;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.model.UserBasicInfo;
import cz.engeto.ja.genesisResources.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) throws SQLException {
        userService.createUser(user);
        return user;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable Long id, @RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) throws SQLException {
        if (detail) {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }
            return ResponseEntity.ok(user);
        } else {
            UserBasicInfo userBasicInfo = userService.getUserByIdSimple(id);
            if (userBasicInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }
            return ResponseEntity.ok(userBasicInfo);
        }
    }

    @GetMapping("/uuid/{uuid}")
    public User getUserByUuid(@PathVariable UUID uuid) throws SQLException {
        return userService.getUserByUuid(uuid);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) throws SQLException {
        if (detail) {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } else {
            List<UserBasicInfo> users = userService.getAllUsersSimple();
            return ResponseEntity.ok(users);
        }
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
