package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public List<User> getAllUsers() {
        return userService.getAll();
    }
}
