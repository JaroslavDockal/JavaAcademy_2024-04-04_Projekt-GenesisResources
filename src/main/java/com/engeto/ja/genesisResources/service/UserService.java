package com.engeto.ja.genesisResources.service;

import com.engeto.ja.genesisResources.model.User;
import com.engeto.ja.genesisResources.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String name, String surname, String personID) {
        User user = new User(name, surname, personID);
        return userRepository.save(user);
    }

    public User getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(UUID uuid, String name, String surname) {
        User user = userRepository.findByUuid(uuid);
        user.setName(name);
        user.setSurname(surname);
        return userRepository.save(user);
    }

    public void deleteUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid);
        userRepository.delete(user);
    }
}

