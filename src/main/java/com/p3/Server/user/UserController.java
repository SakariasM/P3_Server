package com.p3.Server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

   /* @GetMapping        // @getmapping for endpoint of server - get denotes client request to server
    public List<User> getUsers() {
        return userService.getUsers();
    }
    */
    @GetMapping(path = "role/{username}")
    public Map<String, String> getUserRoleByUsername(@PathVariable("username") String username) {
        return userService.getUserRoleByUsername(username);
    }

    @GetMapping(path = "pass/{username}")
    public Map<String, String> getManagerPassByUsername(@PathVariable("username") String username) {
        return userService.getManagerPassByUsername(username);
    }

    @GetMapping(path = "id/{username}")
    public Map<String, Integer> getIdByUsername(@PathVariable("username") String username) {
        return userService.getIdByUsername(username);
    }

    @GetMapping(path = "name/{username}")
    public Map<String, String> getNameByUsername(@PathVariable("username") String username) {
        return userService.getNameByUsername(username);
    }

    @GetMapping(path = "clockInStatus/{username}")
    public Map<String, Boolean> getClockInStatusByUsername(@PathVariable("username") String username) {
        return userService.getClockInStatusByUsername(username);
    }

    @GetMapping(path = "info/users")
    /*public String getAllUsers() {
        return userService.getUsers().toString();
    }*/
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users); // Spring Boot automatically serializes the list to JSON
    }

    @GetMapping(path = "breakStatus/{user_id}")
    public Map<String, Boolean> getBreakStatusById(@PathVariable("user_id") int user_id) {
        return userService.getBreakStatusById(user_id);
    }

    /*
     * POST
     */

    @PostMapping
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

    @PutMapping(path = "clockInStatus/{username}")// TODO vi skal have fikset den inkonsistent måde hvor vi kalder database - Enten username eller user_id ikke begge
    public void updateClockInStatusByUsername(@PathVariable("username") String username,
                                              @RequestParam boolean status ) {
        userService.updateClockInStatusByUsername(username, status);
    }


    @PutMapping(path = "update")
    public void updateUserInfo(@RequestBody User user){
            userService.updateUser(user);
    }

    @PutMapping(path = "breakStatus/{user_id}")
    public void setOnBreakStatusById(@PathVariable("user_id") int user_id,
                                 @RequestParam boolean status){
        userService.setOnBreakStatusById(user_id, status);
    }

    @PutMapping(path="clockInStatus/userId/{user_id}")      // TODO temp path, men conflicter ellers med put på username
    public void setClockedInStatusById(@PathVariable("user_id") int user_id,
                                   @RequestParam boolean status) {
        userService.setClockedInStatusById(user_id, status);
    }
}
