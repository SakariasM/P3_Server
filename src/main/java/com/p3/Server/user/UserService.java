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


    public void updateUser(User user) {
        User dbUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));


        System.out.println("Updating user with ID: " + user.getUserId());
        System.out.println("Provided info - Username: " + user.getUsername() +
                ", Full Name: " + user.getFullName() +
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
        if (user.getFullName() != null && !user.getFullName().isEmpty() && !Objects.equals(dbUser.getFullName(), user.getFullName())) {
            dbUser.setFullName(user.getFullName());
        }

        userRepository.save(dbUser);

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
        String name = userOptional.map(User::getFullName).orElse(null);
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

    public String getNameById(int user_id) {
        Optional<User> userOptional = userRepository.findByUser_id(user_id);
        return userOptional.map(User::getFullName).orElse(null);
    }

    public void checkAndHandleIncompleteClockedIns(){
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if(user.getClockedIn()){
                System.out.println("Changed clockin for user " + user.getUsername());
                user.setClockedIn(false);
            }
            if(user.getOnBreak()){
                System.out.println("Changed onBreak for user " + user.getUsername());
                user.setOnBreak(false);
            }

            userRepository.save(user);
        }
    }
}
