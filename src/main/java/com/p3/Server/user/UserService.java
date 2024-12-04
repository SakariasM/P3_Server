package com.p3.Server.user;

import com.p3.Server.timelog.TimelogService;
import com.p3.Server.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.p3.Server.global.ApiKeyManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final ApiKeyManager apiKeyManager;
    private final UserRepository userRepository;

    @Autowired
    public UserService(ApiKeyManager apiKeyManager, UserRepository userRepository, TimelogService timelogService) {
        this.apiKeyManager = apiKeyManager;
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
        userRepository.save(user);

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
        Integer userId = userOptional.map(User::getUserId).orElse(null);
        return JsonUtil.singleJsonResponse("user_id", userId);
    }

    public Map<String, String> getNameByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        String name = userOptional.map(User::getFullName).orElse(null);
        return JsonUtil.singleJsonResponse("full_name", name);
    }
    public Map<String, String> getNamebyId(int user_id) {
        Optional<User> userOptional = userRepository.findByUser_id(user_id);
        String name = userOptional.map(User::getFull_name).orElse(null);
        return JsonUtil.singleJsonResponse("full_name", name);
    }

    public Map<String, Boolean> getClockInStatusByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Boolean status = userOptional.map(User::getClockedIn).orElse(null);
        return JsonUtil.singleJsonResponse("clocked_in", status);
    }

    public void updateClockInStatusByUsername(String username, boolean status) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("User not found"));
        user.setClockedIn(status);
        userRepository.save(user);
    }

    public Map<String, Boolean> getBreakStatusById(int user_id){
        Optional<User> userOptional = userRepository.findByUser_id(user_id);
        Boolean status = userOptional.map(User::getOnBreak).orElse(null);
        return  JsonUtil.singleJsonResponse("on_break", status);
    }

    public void setOnBreakStatusById(int user_id, boolean status){
        User user = userRepository.findByUser_id(user_id).orElseThrow(() -> new IllegalStateException("User not found"));
        user.setOnBreak(status);
        userRepository.save(user);
    }

    public void setClockedInStatusById(int user_id, boolean status){
        User user = userRepository.findByUser_id(user_id).orElseThrow(() -> new IllegalStateException("User not found"));
        user.setClockedIn(status);
        userRepository.save(user);
    }

    public Map<String, String> loginUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            String apiKey = apiKeyManager.generateApiKey(username);
            return JsonUtil.singleJsonResponse("api_key", apiKey);
        }
        throw new IllegalStateException("User not found");
    }
}
