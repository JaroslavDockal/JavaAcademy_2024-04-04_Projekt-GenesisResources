package cz.engeto.ja.genesisResources.service;

import cz.engeto.ja.genesisResources.model.User;
import cz.engeto.ja.genesisResources.model.UserBasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final String CONNECTION_CONSTANT = "jdbc:mysql://localhost:3306/genesisResources_db?user=root&password=Genesis-2024";
    private Connection connection;

    @Autowired
    private PersonIdService personIdService;

    @PostConstruct
    public void init() throws SQLException {
        connection = DriverManager.getConnection(CONNECTION_CONSTANT);
    }

    public void createUser(User user) throws SQLException {
        if (personIdService.getPersonIds().contains(user.getPersonID())) {
            throw new SQLException("personID already assigned to another user");
        }

        String sql = "INSERT INTO Users (name, surname, personID, uuid) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPersonID());
            statement.setString(4, user.getUuid().toString());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getLong(1));
            }
        }
    }

    public User getUserById(Long id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID"),
                        UUID.fromString(resultSet.getString("uuid"))
                );
            }
        }
        return null;
    }

    public UserBasicInfo getUserByIdSimple(Long id) throws SQLException {
        User user = getUserById(id);
        if (user != null) {
            return UserBasicInfo.fromUser(user);
        }
        return null;
    }

    public User getUserByUuid(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM Users WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID"),
                        UUID.fromString(resultSet.getString("uuid"))
                );
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM Users";
        List<User> allUsers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID"),
                        UUID.fromString(resultSet.getString("uuid"))
                );
                allUsers.add(user);
            }
        }
        return allUsers;
    }

    public List<UserBasicInfo> getAllUsersSimple() throws SQLException {
        String sql = "SELECT * FROM Users";
        List<UserBasicInfo> allUsers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UserBasicInfo userBasicInfo = new UserBasicInfo(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                );
                allUsers.add(userBasicInfo);
            }
        }
        return allUsers;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET name = ?, surname = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getUuid().toString());
            statement.executeUpdate();
        }
    }

    public void deleteUser(UUID uuid) throws SQLException {
        String sql = "DELETE FROM Users WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        }
    }
}
