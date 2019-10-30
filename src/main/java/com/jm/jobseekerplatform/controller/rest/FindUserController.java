package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;
import static java.util.regex.Pattern.matches;

@Controller
@RequestMapping("/api")
public class FindUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/employer_search/{strSearch}")
    public List<User> getEmployerSearch(@PathVariable("strSearch") String strSearch) {
        List<User> employerUsers = new ArrayList<>();
        if(emailVerification(strSearch)) {
            List<User> users = userService.getUserByEmail(strSearch);
            for (User user : users) {
                if (user.getProfile().getTypeName().equals("Работодатель")) {
                    employerUsers.add(user);
                }
            }
            return employerUsers;
        } else {
            List<User> users = userService.getEmployerUserByName(strSearch);
            for (User user : users) {
                if (user.getProfile().getTypeName().equals("Работодатель")) {
                    employerUsers.add(user);
                }
            }
            return employerUsers;
        }
    }

    @GetMapping("/seeker_search/{strSearch}")
    public List<User> getSeekerSearch(@PathVariable("strSearch") String strSearch) {
        List<User> seekerUsers = new ArrayList<>();
        if(emailVerification(strSearch)) {
            List<User> users = userService.getUserByEmail(strSearch);
            for (User user : users) {
                if (user.getProfile().getTypeName().equals("Соискатель")) {
                    seekerUsers.add(user);
                }
            }
            return seekerUsers;
        } else {
            List<User> users = userService.getSeekerUserByName(strSearch);
            for (User user : users) {
                if (user.getProfile().getTypeName().equals("Соискатель")) {
                    seekerUsers.add(user);
                }
            }
            return seekerUsers;
        }
    }

    private boolean emailVerification(String strSearch) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (matches(EMAIL_PATTERN, strSearch)) {
            return true;
        }
        return false;
    }

}
