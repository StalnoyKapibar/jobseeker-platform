package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void registerNewUser(@RequestBody User user) {
        if (userService.validateNewUser(user)) {
            userService.registerNewUser(user);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addUserByAdmin/{check}")
    public void addNewUser(@RequestBody User user, @PathVariable boolean check) {
        if (userService.validateNewUser(user)) {
            userService.addNewUserByAdmin(user,check);
        }
    }
//     проверка валидации user через ajax
    @RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
    public ResponseEntity<Boolean> isExistEmail(@PathVariable String email) {
        if (userService.isExistEmail(email)) {
//            return ResponseEntity.ok("{\"valid\" : \"false\"}");
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUser/{id}")
    public User getUser(@PathVariable(required = false) Long id) {
        return userService.getById(id);
    }

    // отправка приглашения
    @RequestMapping(method = RequestMethod.GET, value = "/inviteFriend/{user}/{friend}")
    public void inviteFriend(@PathVariable String user, @PathVariable String friend) {
        userService.inviteFriend(user, friend);
    }

}
