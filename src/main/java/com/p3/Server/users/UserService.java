package com.p3.Server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();   // Return every user in database <Maybe password should not be included?>
    }

    public void addNewUser(User user) {
        Optional<User> usersOptional = userRepository.findByUsername(user.getUsername());

        if (usersOptional.isPresent()) {    // If username already exists in database
            throw new IllegalStateException("Username already exists");
        }
        userRepository.save(user);         // If it does not exist, it can be added
    }

    public void deleteUser(int userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public void updateUser(int userId, String username, String password, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
            System.out.println(userId + username + password + role);
        if(username != null && !username.isEmpty() && !Objects.equals(user.getUsername(), username)) {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                throw new IllegalStateException("Username already exists");
            }
            user.setUsername(username);
        }

        if(password != null && !password.isEmpty() && !Objects.equals(user.getPassword(), password)) {
            user.setPassword(password);
        }

        if(role != null && !role.isEmpty() && !Objects.equals(user.getRole(), role)) {
            user.setRole(role);
        }
        userRepository.save(user);
    }
}
