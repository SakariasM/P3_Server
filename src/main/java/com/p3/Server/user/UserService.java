package com.p3.Server.user;

import com.p3.Server.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public void updateUser(User user) {
        // Fetch the user from the database
        User dbUser = userRepository.findById(user.getUser_id())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Debugging: Print the provided user information
        System.out.println("Updating user with ID: " + user.getUser_id());
        System.out.println("Provided info - Username: " + user.getUsername() +
                ", Full Name: " + user.getFull_name() +
                ", Role: " + user.getRole() +
                ", Password: " + user.getPassword());

        if (user.getUsername() != null && !user.getUsername().isEmpty() && !Objects.equals(dbUser.getUsername(), user.getUsername())) {
            Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
            if (userOptional.isPresent()) {
                throw new IllegalStateException("Username already exists");
            }
            dbUser.setUsername(user.getUsername());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty() && !Objects.equals(dbUser.getPassword(), user.getPassword())) {
            dbUser.setPassword(user.getPassword());
        }

        if (user.getRole() != null && !user.getRole().isEmpty() && !Objects.equals(dbUser.getRole(), user.getRole())) {
            dbUser.setRole(user.getRole());
        }
        if (user.getFull_name() != null && !user.getFull_name().isEmpty() && !Objects.equals(dbUser.getFull_name(), user.getFull_name())) {
            dbUser.setFull_name(user.getFull_name());
        }

        userRepository.save(dbUser);

        System.out.println("User updated successfully: " + dbUser);
    }

    public Map<String, String> getUserRoleByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        String role = userOptional.map(User::getRole).orElse(null);
        return JsonUtil.singleJsonResponse("role", role);
    }

    public Map<String, String> getManagerPassByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        String pass = userOptional.map(User::getPassword).orElse(null);
        return JsonUtil.singleJsonResponse("password", pass);
    }

    public Map<String, Integer> getIdByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Integer userId = userOptional.map(User::getUser_id).orElse(null);
        return JsonUtil.singleJsonResponse("user_id", userId);
    }

    public Map<String, String> getNameByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        String name = userOptional.map(User::getFull_name).orElse(null);
        return JsonUtil.singleJsonResponse("full_name", name);
    }

    public Map<String, Boolean> getClockInStatusByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Boolean status = userOptional.map(User::getClocked_in).orElse(null);
        return JsonUtil.singleJsonResponse("clocked_in", status);
    }

    public void updateClockInStatusByUsername(String username, boolean status) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("User not found"));
        user.setClocked_in(status);
        userRepository.save(user);
    }

    public Map<String, Boolean> getBreakStatusById(int user_id){
        Optional<User> userOptional = userRepository.findByUser_id(user_id);
        Boolean status = userOptional.map(User::getOn_break).orElse(null);
        return  JsonUtil.singleJsonResponse("on_break", status);
    }

    public void setOnBreakStatusById(int user_id, boolean status){
        User user = userRepository.findByUser_id(user_id).orElseThrow(() -> new IllegalStateException("User not found"));
        user.setOn_break(status);
        userRepository.save(user);
    }

    public void setClockedInStatusById(int user_id, boolean status){
        User user = userRepository.findByUser_id(user_id).orElseThrow(() -> new IllegalStateException("User not found"));
        user.setClocked_in(status);
        userRepository.save(user);
    }
}
