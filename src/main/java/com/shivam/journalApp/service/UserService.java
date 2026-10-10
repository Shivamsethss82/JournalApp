package com.shivam.journalApp.service;

import com.shivam.journalApp.entity.User;
import com.shivam.journalApp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Naya user: password encode + default role USER
    public User saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }

    public User saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER", "ADMIN"));
        return userRepository.save(user);
    }

    // Update: password sirf tab encode hoga jab body me naya password aaya ho
    public User updateUser(User userInDb, User newData) {
        if (newData.getUserName() != null && !newData.getUserName().isBlank()) {
            userInDb.setUserName(newData.getUserName());
        }
        if (newData.getPassword() != null && !newData.getPassword().isBlank()) {
            userInDb.setPassword(passwordEncoder.encode(newData.getPassword()));
        }
        return userRepository.save(userInDb);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}