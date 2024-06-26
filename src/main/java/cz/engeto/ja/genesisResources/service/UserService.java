package cz.engeto.ja.genesisResources.service;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.model.UserBasicInfo;
import cz.engeto.ja.genesisResources.util.Settings;
import cz.engeto.ja.genesisResources.util.AppLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final String CONNECTION_STRING = Settings.CONNECTION_STRING;

    @Autowired
    private PersonIdService personIdService;

    public UserService(PersonIdService personIdService) {
        this.personIdService = personIdService;
    }

    private Connection getConnection() throws SQLException {
        AppLogger.log("Connecting to database...");
        return DriverManager.getConnection(CONNECTION_STRING);
    }

    public void createUser(User user) throws SQLException {
        AppLogger.log("Creating user: " + user);
        String sql = "INSERT INTO Users (name, surname, personID, uuid) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (personIdService.isPersonIdUsedByOtherUser(user.getPersonID())) {
                AppLogger.log("PersonID " + user.getPersonID() + " already assigned to another user");
                throw new SQLException("personID already assigned to another user");
            }

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPersonID());
            statement.setString(4, user.getUuid().toString());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getLong(1));
                AppLogger.log("User created with ID: " + user.getId());
            }
        } catch (SQLException e) {
            AppLogger.log("Failed to create user: " + e.getMessage());
            throw new SQLException("Failed to create user", e);
        }
    }

    public User getUserByPersonId(String personID) throws SQLException {
        AppLogger.log("Retrieving user by personID: " + personID);
        String sql = "SELECT * FROM Users WHERE personID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, personID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID"),
                        resultSet.getString("uuid")
                );
                AppLogger.log("User found: " + user);
                return user;
            }
        } catch (SQLException e) {
            AppLogger.log("Failed to retrieve user by personID: " + e.getMessage());
            throw new SQLException("Failed to retrieve user by personID", e);
        }
        AppLogger.log("No user found with personID: " + personID);
        return null;
    }

    public User getUserById(Long id) throws SQLException {
        AppLogger.log("Retrieving user by ID: " + id);
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID"),
                        resultSet.getString("uuid")
                );
                AppLogger.log("User found: " + user);
                return user;
            }
        } catch (SQLException e) {
            AppLogger.log("Failed to retrieve user by ID: " + e.getMessage());
            throw new SQLException("Failed to retrieve user by ID", e);
        }
        AppLogger.log("No user found with ID: " + id);
        return null;
    }

    public UserBasicInfo getUserByIdSimple(Long id) throws SQLException {
        try {
            User user = getUserById(id);
            if (user != null) {
                UserBasicInfo userBasicInfo = UserBasicInfo.fromUser(user);
                AppLogger.log("Basic info of user found: " + userBasicInfo);
                return userBasicInfo;
            }
        } catch (SQLException e) {
            AppLogger.log("Failed to retrieve user (basic info) by ID: " + e.getMessage());
            throw new SQLException("Failed to retrieve user (basic info) by ID", e);
        }
        AppLogger.log("No basic info found for user with ID: " + id);
        return null;
    }

    public User getUserByUuid(UUID uuid) throws SQLException {
        AppLogger.log("Retrieving user by UUID: " + uuid);
        String sql = "SELECT * FROM Users WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID"),
                        resultSet.getString("uuid")
                );
                AppLogger.log("User found: " + user);
                return user;
            }
        } catch (SQLException e) {
            AppLogger.log("Failed to retrieve user by UUID: " + e.getMessage());
            throw new SQLException("Failed to retrieve user by UUID", e);
        }
        AppLogger.log("No user found with UUID: " + uuid);
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        AppLogger.log("Retrieving all users");
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID"),
                        resultSet.getString("uuid")
                );
                allUsers.add(user);
            }
        } catch (SQLException e) {
            AppLogger.log("Failed to retrieve all users: " + e.getMessage());
            throw new SQLException("Failed to retrieve all users (full info)", e);
        }
        AppLogger.log("All users retrieved: " + allUsers);
        return allUsers;
    }

    public List<UserBasicInfo> getAllUsersSimple() throws SQLException {
        AppLogger.log("Retrieving all users (basic info)");
        List<UserBasicInfo> allUsers = new ArrayList<>();
        String sql = "SELECT id, name, surname FROM Users";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UserBasicInfo userBasicInfo = new UserBasicInfo(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                );
                allUsers.add(userBasicInfo);
            }
        } catch (SQLException e) {
            AppLogger.log("Failed to retrieve all users (basic info): " + e.getMessage());
            throw new SQLException("Failed to retrieve all users (basic info)", e);
        }
        AppLogger.log("All users (basic info) retrieved: " + allUsers);
        return allUsers;
    }

    public void updateUser(User user) throws SQLException {
        AppLogger.log("Present user: " + getUserById(user.getId()));
        AppLogger.log("Updating user: " + user);
        String sql = "UPDATE Users SET name = ?, surname = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
            AppLogger.log("User updated: " + UserBasicInfo.fromUser(user));
        } catch (SQLException e) {
            AppLogger.log("Failed to update user: " + e.getMessage());
            throw new SQLException("Failed to update user", e);
        }
    }

    public void deleteUser(Long id) throws SQLException {
        AppLogger.log("Deleting user with ID: " + id);
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            AppLogger.log("User deleted with ID: " + id);
        } catch (SQLException e) {
            AppLogger.log("Failed to delete user: " + e.getMessage());
            throw new SQLException("Failed to delete user", e);
        }
    }
}
