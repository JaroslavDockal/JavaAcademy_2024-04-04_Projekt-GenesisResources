package cz.engeto.ja.genesisResources.controller;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.model.UserBasicInfo;
import cz.engeto.ja.genesisResources.model.UserCreateData;
import cz.engeto.ja.genesisResources.model.UserUpdateData;
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

/**
 * Controller for handling user-related operations.
 */
@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonIdService personIdService;

    /**
     * Constructor for UserController.
     * @param userService the UserService instance
     * @param personIdService the PersonIdService instance
     */
    public UserController(UserService userService, PersonIdService personIdService) {
        this.userService = userService;
        this.personIdService = personIdService;
    }

    /**
     * Endpoint to create a new user.
     * @param userData Object containing user's name, surname, and personID
     * @return ResponseEntity with appropriate status and message
     */
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody UserCreateData userData) {
        AppLogger.info("Request to create user with data: " + userData);

        String name = userData.getName();
        String surname = userData.getSurname();
        String personID = userData.getPersonID();

        if (name == null || surname == null || personID == null || personID.isEmpty()) {
            AppLogger.warn("Invalid input: Name, surname, or personID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: Name, surname, or personID is empty");
        }

        try {
            if (!(personID.length() == 12 && personID.matches("[0-9a-zA-Z]+"))) {
                AppLogger.warn("Invalid personID: " + personID + ", must be 12 characters long and alphanumeric");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid personID");
            }

            if (!personIdService.getPersonIds().contains(personID)) {
                AppLogger.warn("Invalid personID: " + personID + ", not in the list of available personIDs");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid personID");
            }

            User user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setPersonID(personID);

            userService.createUser(user);
            personIdService.markPersonIdAsAssigned(personID);

            AppLogger.info("User created: " + user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (SQLException e) {
            AppLogger.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to retrieve a user by ID.
     * @param id the ID of the user
     * @param detail whether to fetch detailed user information
     * @return ResponseEntity with user information or error message
     */
    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable String id, @RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        AppLogger.info("Request to get user by ID: " + id + ", detail: " + detail);
        if (id == null || id.isEmpty()) {
            AppLogger.warn("Invalid input: ID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: ID is empty");
        }
        if (!isNumeric(id)) {
            AppLogger.warn("Invalid ID format: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID format");
        }
        try {
            Long userId = Long.parseLong(id);
            if (detail) {
                User user = userService.getUserById(userId);
                if (user == null) {
                    AppLogger.warn("User not found with ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
                }
                return ResponseEntity.ok(user);
            } else {
                UserBasicInfo userBasicInfo = userService.getUserByIdSimple(userId);
                if (userBasicInfo == null) {
                    AppLogger.warn("User not found with ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
                }
                return ResponseEntity.ok(userBasicInfo);
            }
        } catch (SQLException e) {
            AppLogger.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve a user by UUID.
     * @param uuidStr the UUID string of the user
     * @return ResponseEntity with user information or error message
     */
    @GetMapping("/user/uuid/{uuid}")
    public ResponseEntity<?> getUserByUuid(@PathVariable String uuidStr) {
        AppLogger.info("Request to get user by UUID: " + uuidStr);
        if (uuidStr == null || uuidStr.isEmpty()) {
            AppLogger.warn("Invalid input: UUID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: UUID is empty");
        }
        try {
            UUID uuid = UUID.fromString(uuidStr);
            User user = userService.getUserByUuid(uuid);
            if (user == null) {
                AppLogger.warn("User not found with UUID: " + uuid);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with uuid " + uuid);
            }
            AppLogger.info("User found: " + user);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            AppLogger.warn("Invalid UUID format: " + uuidStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format");
        } catch (SQLException e) {
            AppLogger.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve all users.
     * @param detail whether to fetch detailed user information
     * @return ResponseEntity with list of users or error message
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        AppLogger.info("Request to get all users, detail: " + detail);
        try {
            if (detail) {
                List<User> users = userService.getAllUsers();
                return ResponseEntity.ok(users);
            } else {
                List<UserBasicInfo> users = userService.getAllUsersSimple();
                return ResponseEntity.ok(users);
            }
        } catch (SQLException e) {
            AppLogger.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to update a user.
     * @param userData the object containing ID, name, and surname of the user to update
     * @return ResponseEntity with updated user information or error message
     */
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateData userData) {
        String id = userData.getId();
        String name = userData.getName();
        String surname = userData.getSurname();

        AppLogger.info("Request to update user with ID: " + id + ", new name: " + name + ", new surname: " + surname);

        if (id == null || id.isEmpty() || name == null || name.isEmpty() || surname == null || surname.isEmpty()) {
            AppLogger.warn("Invalid input: ID, name, or surname is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: ID, name, or surname is empty");
        }
        if (!isNumeric(id)) {
            AppLogger.warn("Invalid ID format: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID format");
        }
        try {
            Long userId = Long.parseLong(id);

            User userToUpdate = userService.getUserById(userId);
            if (userToUpdate == null) {
                AppLogger.warn("User not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }

            userToUpdate.setName(name);
            userToUpdate.setSurname(surname);

            userService.updateUser(userToUpdate);

            UserBasicInfo userBasicInfo = userService.getUserByIdSimple(userId);
            AppLogger.info("User updated: " + userBasicInfo);
            return ResponseEntity.ok(userBasicInfo);

        } catch (SQLException e) {
            AppLogger.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to delete a user.
     * @param id the ID of the user to delete
     * @return ResponseEntity with appropriate status or error message
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        AppLogger.info("Request to delete user with ID: " + id);
        if (id == null || id.isEmpty()) {
            AppLogger.warn("Invalid input: ID is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: ID is empty");
        }
        if (!isNumeric(id)) {
            AppLogger.warn("Invalid ID format: " + id);
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
            AppLogger.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Checks if a string is numeric.
     * @param str the string to check
     * @return true if the string is numeric, false otherwise
     */
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
