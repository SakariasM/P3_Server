package com.p3.Server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/user")
public class UserController {

    private final UserService userService;

    @Autowired      // Auto sets the userClassService as the constant
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    * GET
    */

    // gets List of all users
    @GetMapping(path = "info/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // Gets apikey based on username
    @GetMapping(path = "apiKey/{username}")
    public Map<String, String> getApiKey(@PathVariable("username") String username) {
        return userService.loginUser(username);

    }

    // Get mapping for requests of single piece of user information from username
    @GetMapping("{username}/{requestedInformation}")
    public ResponseEntity<?> getUserInformation(
            @PathVariable("username") String username,
            @PathVariable("requestedInformation") String requestedInformation) {
        switch (requestedInformation){
            case "role":
                return ResponseEntity.ok(userService.getUserRoleByUsername(username));
            case "password":
                return ResponseEntity.ok(userService.getManagerPassByUsername(username));
            case "id":
                return ResponseEntity.ok(userService.getIdByUsername(username));
            case "clockInStatus":
                return ResponseEntity.ok(userService.getClockInStatusByUsername(username));
            case "fullName":
                return ResponseEntity.ok(userService.getNameByUsername(username));
            default:
                return ResponseEntity.badRequest().body(Map.of("error", "Unknown requested information: " + requestedInformation));

        }
    }

    @GetMapping(path="id/{userId}/{requestedInformation}")
    public ResponseEntity<?> getUserInformationById(
            @PathVariable("userId") int userId,
            @PathVariable("requestedInformation") String requestedInformation){
        switch (requestedInformation){
            case "breakStatus":
                return ResponseEntity.ok(userService.getBreakStatusById(userId));
            case "fullName":
                return ResponseEntity.ok(userService.getNameById(userId));
            default:
                return ResponseEntity.badRequest().body(Map.of("error", "Unknown requested information: " + requestedInformation));
        }
    }

    /*
     * POST
     */

    @PostMapping("newUser")
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    /*
     * DELETE
     */

    @DeleteMapping(path = "{user_id}")  // path will be api/user/"user_id"
    public void deleteUser(@PathVariable("user_id") int user_id) {
        userService.deleteUser(user_id);
    }

    /*
     * PUT
     */


    @PutMapping(path = "update")
    public void updateUserInfo(@RequestBody User user){
        userService.updateUser(user);
    }


    @PutMapping(path = "clockInStatus/{username}")
    public void updateClockInStatusByUsername(@PathVariable("username") String username,
                                              @RequestParam boolean status ) {
        userService.updateClockInStatusByUsername(username, status);
    }

    @PutMapping(path = "breakStatus/{user_id}")
    public void setOnBreakStatusById(@PathVariable("user_id") int user_id,
                                 @RequestParam boolean status){
        userService.setOnBreakStatusById(user_id, status);
    }

    @PutMapping(path="clockInStatus/userId/{user_id}")
    public void setClockedInStatusById(@PathVariable("user_id") int user_id,
                                   @RequestParam boolean status) {
        userService.setClockedInStatusById(user_id, status);
    }
}
