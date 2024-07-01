package cz.engeto.ja.genesisResources.controller;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.model.UserBasicInfo;
import cz.engeto.ja.genesisResources.service.PersonIdService;
import cz.engeto.ja.genesisResources.service.UserService;
import cz.engeto.ja.genesisResources.util.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonIdService personIdService;

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        Logger.log("Request to create user: " + user);
        try {
            String personID = user.getPersonID();

            if (userService.getUserByPersonId(personID) != null) {
                Logger.log("Conflict: personID " + personID + " already assigned to another user");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("personID already assigned to another user");
            }

            if (!(personID.length() == 12 && personID.matches("[0-9a-zA-Z]+"))) {
                Logger.log("Invalid personID: " + personID);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid personID");
            }

            if (!personIdService.getPersonIds().contains(personID)) {
                Logger.log("Invalid personID: " + personID);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid personID");
            }

            user.setPersonID(personID);
            userService.createUser(user);
            personIdService.markPersonIdAsAssigned(personID);

            Logger.log("User created: " + user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (SQLException e) {
            Logger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable Long id, @RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        Logger.log("Request to get user by ID: " + id + ", detail: " + detail);
        try {
            if (detail) {
                User user = userService.getUserById(id);
                if (user == null) {
                    Logger.log("User not found with ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
                }
                Logger.log("User found: " + user);
                return ResponseEntity.ok(user);
            } else {
                UserBasicInfo userBasicInfo = userService.getUserByIdSimple(id);
                if (userBasicInfo == null) {
                    Logger.log("User not found with ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
                }
                Logger.log("User basic info found: " + userBasicInfo);
                return ResponseEntity.ok(userBasicInfo);
            }
        } catch (SQLException e) {
            Logger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/uuid/{uuid}")
    public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
        Logger.log("Request to get user by UUID: " + uuid);
        try {
            User user = userService.getUserByUuid(uuid);
            if (user == null) {
                Logger.log("User not found with UUID: " + uuid);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with uuid " + uuid);
            }
            Logger.log("User found: " + user);
            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            Logger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        Logger.log("Request to get all users, detail: " + detail);
        try {
            if (detail) {
                List<User> users = userService.getAllUsers();
                Logger.log("All users retrieved: " + users);
                return ResponseEntity.ok(users);
            } else {
                List<UserBasicInfo> users = userService.getAllUsersSimple();
                Logger.log("All users (basic info) retrieved: " + users);
                return ResponseEntity.ok(users);
            }
        } catch (SQLException e) {
            Logger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        Logger.log("Request to update user with ID: " + id + ", new data: " + user);
        try {
            user.setId(id);
            userService.updateUser(user);

            UserBasicInfo userBasicInfo = userService.getUserByIdSimple(id);
            if (userBasicInfo == null) {
                Logger.log("User not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }
            Logger.log("User updated: " + userBasicInfo);
            return ResponseEntity.ok(userBasicInfo);
        } catch (SQLException e) {
            Logger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Logger.log("Request to delete user with ID: " + id);
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                Logger.log("User not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }

            userService.deleteUser(id);
            Logger.log("User deleted with ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            Logger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
