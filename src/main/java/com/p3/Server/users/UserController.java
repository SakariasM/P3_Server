package com.p3.Server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping        // @getmapping for endpoint of server - get denotes client request to server
    public List<User> getUsers() {
        return userService.getUsers();
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

}
