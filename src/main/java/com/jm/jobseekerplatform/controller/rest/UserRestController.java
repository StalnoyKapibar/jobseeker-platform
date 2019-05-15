package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.User;
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
        if (userService.validateNewUser(user)) {
            userService.registerNewUser(user);
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
