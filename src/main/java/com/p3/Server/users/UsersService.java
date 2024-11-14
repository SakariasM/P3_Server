package com.p3.Server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getUsers() {
        return usersRepository.findAll();   // Return every user in database <Maybe password should not be included?>
    }

    public void addNewUser(Users user) {
        Optional<Users> usersOptional = usersRepository.findByUsername(user.getUsername());

        if (usersOptional.isPresent()) {    // If username already exists in database
            throw new IllegalStateException("Username already exists");
        }
        usersRepository.save(user);         // If it does not exist, it can be added
    }

    public void deleteUser(int userId) {
        boolean exists = usersRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User not found");
        }
        usersRepository.deleteById(userId);
    }

    public void updateUser(int userId, String username, String password, String role) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
            System.out.println(userId + username + password + role);
        if(username != null && !username.isEmpty() && !Objects.equals(user.getUsername(), username)) {
            Optional<Users> userOptional = usersRepository.findByUsername(username);
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
        usersRepository.save(user);
    }
}
