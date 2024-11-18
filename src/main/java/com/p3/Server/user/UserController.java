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

    @PutMapping(path = "{user_id}") // TODO Add optional for clockedIn, onBreak, loggedIn - EVT gør til request body?
    public void updateUser(@PathVariable("user_id") int user_id,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) String role) {
        userService.updateUser(user_id, username, password, role);
    }

    @PutMapping(path = "clockInStatus/{username}")
    public void updateClockInStatusByUsername(@PathVariable("username") String username,
                                              @RequestParam boolean status ) {
        userService.updateClockInStatusByUsername(username, status);
    }

}
