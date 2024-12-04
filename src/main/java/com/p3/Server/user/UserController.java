package com.p3.Server.user;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path = "info/users")      // @getmapping for endpoint of server - get denotes client request to server
    public List<User> getUsers() {
        return userService.getUsers();
    }


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

    @GetMapping(path = "name/{id}")
    public Map<String, String> getNameByID(@PathVariable("username") String username) {
        return userService.getNameByUsername(username);
    }

    @GetMapping(path = "clockInStatus/{username}")
    public Map<String, Boolean> getClockInStatusByUsername(@PathVariable("username") String username) {
        return userService.getClockInStatusByUsername(username);
    }

    @GetMapping(path = "breakStatus/{user_id}")
    public Map<String, Boolean> getBreakStatusById(@PathVariable("user_id") int user_id) {
        return userService.getBreakStatusById(user_id);
    }

    @GetMapping(path = "fullName/{username}")
    public Map<String, String> getFullNameByUsername(@PathVariable("username") String username) {
        return userService.getNameByUsername(username);
    }

    @GetMapping(path = "fullNameId/{user_id}")
    public Map<String, String> getFullNameByid(@PathVariable("user_id") int user_id) {
        return userService.getNamebyId(user_id);
    }
    @GetMapping(path = "apiKey/{username}")
    public Map<String, String> getApiKey(@PathVariable("username") String username) {
        return userService.loginUser(username);

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

    //TODO HVORFOR ÆNDRER DU .updateUser, uden at apply ændringerne på frontend??? NU DET JO BROKEN + hvad bruger vi update user til udover "rediger medarbejder"
   /* @PutMapping(path = "{user_id}") // TODO Add optional for clockedIn, onBreak, loggedIn - EVT gør til request body?
    public void updateUser(@PathVariable("user_id") int user_id,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) String role) {
        userService.updateUser(user_id, username, password, role);
    } */

    @PutMapping(path = "clockInStatus/{username}")// TODO vi skal have fikset den inkonsistent måde hvor vi kalder database - Enten username eller user_id ikke begge
    public void updateClockInStatusByUsername(@PathVariable("username") String username,
                                              @RequestParam boolean status ) {
        userService.updateClockInStatusByUsername(username, status);
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
