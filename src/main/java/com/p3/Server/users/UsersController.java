package com.p3.Server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/user")
public class UsersController {

    private final UsersService usersService;

    @Autowired      // Auto sets the userClassService as the constant
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /*
    * GET
    */

    @GetMapping        // @getmapping for endpoint of server - get denotes client request to server
    public List<Users> getUsers() {
        return usersService.getUsers();
    }

    /*
     * POST
     */

    @PostMapping
    public void registerNewUser(@RequestBody Users user) {
        usersService.addNewUser(user);
    }

    /*
     * DELETE
     */

    @DeleteMapping(path = "{user_id}")  // path will be api/user/"user_id"
    public void deleteUser(@PathVariable("user_id") int user_id) {
        usersService.deleteUser(user_id);
    }

    /*
     * PUT
     */

    @PutMapping(path = "{user_id}")
    public void updateUser(@PathVariable("user_id") int user_id,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) String role) {
        usersService.updateUser(user_id, username, password, role);
    }

}
