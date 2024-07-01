package cz.engeto.ja.genesisResources.controller;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.model.UserBasicInfo;
import cz.engeto.ja.genesisResources.service.PersonIdService;
import cz.engeto.ja.genesisResources.service.UserService;
import cz.engeto.ja.genesisResources.util.AppLogger;

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

    public UserController(UserService userService, PersonIdService personIdService) {
        this.userService = userService;
        this.personIdService = personIdService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        AppLogger.log("Request to create user: " + user);
        if (user == null || user.getPersonID() == null || user.getPersonID().isEmpty()) {
            AppLogger.log("Invalid input: User or personID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: User or personID is empty");
        }
        try {
            String personID = user.getPersonID();

            if (userService.getUserByPersonId(personID) != null) {
                AppLogger.log("Conflict: personID " + personID + " already assigned to another user");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("personID already assigned to another user");
            }

            if (!(personID.length() == 12 && personID.matches("[0-9a-zA-Z]+"))) {
                AppLogger.log("Invalid personID: " + personID + ", must be 12 characters long and alphanumeric");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid personID");
            }

            if (!personIdService.getPersonIds().contains(personID)) {
                AppLogger.log("Invalid personID: " + personID + ", not in the list of available personIDs");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid personID");
            }

            user.setPersonID(personID);
            userService.createUser(user);
            personIdService.markPersonIdAsAssigned(personID);

            AppLogger.log("User created: " + user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (SQLException e) {
            AppLogger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable String id, @RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        AppLogger.log("Request to get user by ID: " + id + ", detail: " + detail);
        if (id == null || id.isEmpty()) {
            AppLogger.log("Invalid input: ID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: ID is empty");
        }
        if (!isNumeric(id)) {
            AppLogger.log("Invalid ID format: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID format");
        }
        try {
            Long userId = Long.parseLong(id);
            if (detail) {
                User user = userService.getUserById(userId);
                if (user == null) {
                    AppLogger.log("User not found with ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
                }
                return ResponseEntity.ok(user);
            } else {
                UserBasicInfo userBasicInfo = userService.getUserByIdSimple(userId);
                if (userBasicInfo == null) {
                    AppLogger.log("User not found with ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
                }
                return ResponseEntity.ok(userBasicInfo);
            }
        } catch (SQLException e) {
            AppLogger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/uuid/{uuid}")
    public ResponseEntity<?> getUserByUuid(@PathVariable String uuidStr) {
        AppLogger.log("Request to get user by UUID: " + uuidStr);
        if (uuidStr == null || uuidStr.isEmpty()) {
            AppLogger.log("Invalid input: UUID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: UUID is empty");
        }
        try {
            UUID uuid = UUID.fromString(uuidStr);
            User user = userService.getUserByUuid(uuid);
            if (user == null) {
                AppLogger.log("User not found with UUID: " + uuid);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with uuid " + uuid);
            }
            AppLogger.log("User found: " + user);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            AppLogger.log("Invalid UUID format: " + uuidStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format");
        } catch (SQLException e) {
            AppLogger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        AppLogger.log("Request to get all users, detail: " + detail);
        try {
            if (detail) {
                List<User> users = userService.getAllUsers();
                return ResponseEntity.ok(users);
            } else {
                List<UserBasicInfo> users = userService.getAllUsersSimple();
                return ResponseEntity.ok(users);
            }
        } catch (SQLException e) {
            AppLogger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        AppLogger.log("Request to update user with ID: " + id + ", new data: " + UserBasicInfo.fromUser(user));
        if (id == null || id.isEmpty() || user == null) {
            AppLogger.log("Invalid input: ID or user data is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: ID or user data is empty");
        }
        if (!isNumeric(id)) {
            AppLogger.log("Invalid ID format: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID format");
        }
        try {
            Long userId = Long.parseLong(id);
            user.setId(userId);
            userService.updateUser(user);

            UserBasicInfo userBasicInfo = userService.getUserByIdSimple(userId);
            if (userBasicInfo == null) {
                AppLogger.log("User not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }
            AppLogger.log("User updated: " + userBasicInfo);
            return ResponseEntity.ok(userBasicInfo);
        } catch (SQLException e) {
            AppLogger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        AppLogger.log("Request to delete user with ID: " + id);
        if (id == null || id.isEmpty()) {
            AppLogger.log("Invalid input: ID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: ID is empty");
        }
        if (!isNumeric(id)) {
            AppLogger.log("Invalid ID format: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID format");
        }
        try {
            Long userId = Long.parseLong(id);
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }

            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            AppLogger.log("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
