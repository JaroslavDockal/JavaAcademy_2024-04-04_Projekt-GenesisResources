package cz.engeto.ja.genesisResources.controller;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.model.UserBasicInfo;
import cz.engeto.ja.genesisResources.service.PersonIdService;
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
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonIdService personIdService;

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            String personID = user.getPersonID();

            if (!personIdService.getPersonIds().contains(personID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid personID");
            }

            if (userService.getUserByPersonId(personID) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("personID already assigned to another user");
            }

            user.setPersonID(personID);
            userService.createUser(user);
            personIdService.markPersonIdAsAssigned(personID);

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //TODO: Podle zadání by to měl být string, ale v db je to long...
    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable Long id, @RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        try {
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
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/uuid/{uuid}")
    public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
        try {
            User user = userService.getUserByUuid(uuid);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with uuid " + uuid);
            }
            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "detail", required = false, defaultValue = "false") boolean detail) {
        try {
            if (detail) {
                List<User> users = userService.getAllUsers();
                return ResponseEntity.ok(users);
            } else {
                List<UserBasicInfo> users = userService.getAllUsersSimple();
                return ResponseEntity.ok(users);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            userService.updateUser(user);

            UserBasicInfo userBasicInfo = userService.getUserByIdSimple(id);
            if (userBasicInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
            }
            return ResponseEntity.ok(userBasicInfo);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
