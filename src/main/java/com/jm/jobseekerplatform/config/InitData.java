package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitData {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    public void initData() {
        initUserRoles();
        initUsers();
    }

    public void initUserRoles() {
        userRoleService.add(new UserRole("ROLE_ADMIN"));
        userRoleService.add(new UserRole("ROLE_USER"));
        userRoleService.add(new UserRole("ROLE_EMPLOYER"));
    }

    public void initUsers() {
        UserRole roleAdmin = userRoleService.findByAuthority("ROLE_ADMIN");
        userService.add(new User("admin", userService.encodePassword("admin"), "admin@mail.ru", roleAdmin, null));

        UserRole roleUser = userRoleService.findByAuthority("ROLE_USER");
        userService.add(new User("user", userService.encodePassword("user"), "user@mail.ru", roleUser, null));

        UserRole roleEmployer = userRoleService.findByAuthority("ROLE_EMPLOYER");
        userService.add(new User("employer", userService.encodePassword("employer"), "employer@mail.ru", roleEmployer, null));
    }

}
