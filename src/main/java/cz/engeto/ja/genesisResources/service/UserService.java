package cz.engeto.ja.genesisResources.service;

import cz.engeto.ja.genesisResources.model.User;
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

    @PostConstruct
    public void init() throws SQLException {
        connection = DriverManager.getConnection(CONNECTION_CONSTANT);
    }

    //TODO Nějak jsem do****l id - vrací to null i když v db je...
    //TODO Při každém zápisu musíme zkontrolovat, zda personID nebylo přiřazeno již jinému záznamu.
    public void createUser(User user) throws SQLException {
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

    //TODO api/v1/users/{ID}?detail=true -> vrátí rozšířený objekt
    public User getUserById(Long id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        //resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("personID")
                        //UUID.fromString(resultSet.getString("uuid"))
                );
            }
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

    //TODO api/v1/users?detail=true -> vrátí rozšířený objekt
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
