package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.MailService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
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

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/block/{id}/{periodInDays}")
    public ResponseEntity blockUser(@PathVariable("id") Long id,
                                    @PathVariable Integer periodInDays) {
        User user = userService.getById(id);
        user.setEnabled(false);
        userService.update(user);

        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        if (!allPrincipals.isEmpty()) {
            allPrincipals.forEach(o -> {
                User u = (User) o;
                if (u.getId().equals(user.getId())) {
                    List<SessionInformation> allSessions = sessionRegistry.getAllSessions(u, false);
                    allSessions.forEach(SessionInformation::expireNow);
                }
            });
        }

        Profile profile = user.getProfile();
        if (periodInDays == 0) {
            profileService.blockPermanently(profile);
        }
        if (periodInDays > 0 && periodInDays < 15) {
            profileService.blockTemporary(profile, periodInDays);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/unblock/{id}")
    public ResponseEntity unblockUser(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        user.setEnabled(true);
        userService.update(user);

        Profile profile = user.getProfile();
        profileService.unblock(profile);

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
            userService.addNewUserByAdmin(user, check);
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

    // востановление пароля
    @RequestMapping(method = RequestMethod.GET, value = "/password_reset/{token}/{password}")
    public void newPassword(@PathVariable String token, @PathVariable char[] password) {
        userService.passwordReset(token, password);
    }

    @GetMapping("/get-user-authority")
    public GrantedAuthority getUserAuthority(Authentication authentication) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User currUser = (User) authentication.getPrincipal();
            return currUser.getAuthority();
        }
        return null;
    }
}
