package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.service.impl.MailService;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }


    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/enabled/{id}/{enabled}")
    public ResponseEntity enabledUser(@PathVariable("id") Long id,
                                      @PathVariable boolean enabled) {
        User user = userService.getById(id);
        user.setEnabled(enabled);
        userService.update(user);
        return new ResponseEntity(HttpStatus.OK);
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
        return ResponseEntity.ok(userService.isExistEmail(email));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUser/{userId}")
    public User getUser(@PathVariable(required = false) Long userId) {
        return userService.getById(userId);
    }

    // отправка приглашения
    @RequestMapping(method = RequestMethod.GET, value = "/inviteFriend/{user}/{friend}")
    public void inviteFriend(@PathVariable String user, @PathVariable String friend) {
        mailService.sendFriendInvitaionEmail(user, friend);
    }
    //запрос на востановление пароля
    @RequestMapping(method = RequestMethod.GET, value = "/recovery/{email}")
    public ResponseEntity<Boolean> recoveryPassRequest(@PathVariable String email) {
        return ResponseEntity.ok(userService.recoveryPassRequest(email));
    }
    //востановление пароля
    @RequestMapping(method = RequestMethod.GET, value = "/password_reset/{token}/{password}")
    public void newPassword(@PathVariable String token, @PathVariable char[] password) {
        userService.passwordReset(token,password);
    }
}
