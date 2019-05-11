package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Employer;
import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.service.impl.EmployerService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void registerNewUser(@RequestBody User user) {
        if (user.getLogin().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getAuthority().getAuthority().isEmpty()) {
            throw new RuntimeException("Some fields is empty");
        }

        if (!userService.isExistLogin(user.getLogin()) && !userService.isExistEmail(user.getEmail())) {
            userService.registerNewUser(user);
        } else {
            throw new RuntimeException("User's login or email already exist");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login/{login}")
    public Object isExistLogin(@PathVariable String login) {
        if (userService.isExistLogin(login)) {
            return new Object() {
                String valid = "false";
                public String getValid() {
                    return valid;
                }
            };
        } else {
            return new Object() {
                String valid = "true";
                public String getValid() {
                    return valid;
                }
            };
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
    public Object isExistEmail(@PathVariable String email) {
        if (userService.isExistEmail(email)) {
            return new Object() {
                String valid = "false";
                public String getValid() {
                    return valid;
                }
            };
        } else {
            return new Object() {
                String valid = "true";
                public String getValid() {
                    return valid;
                }
            };
        }
    }

}
