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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final SeekerService seekerService;
    private final EmployerService employerService;
    private final UserRoleService userRoleService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");
    private UserRole roleEmployer = new UserRole("ROLE_EMPLOYER");

    @Autowired
    public UserRestController(UserService userService, SeekerService seekerService, EmployerService employerService, UserRoleService userRoleService) {
        this.userService = userService;
        this.seekerService = seekerService;
        this.employerService = employerService;
        this.userRoleService = userRoleService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void addNewUser(@RequestBody User user) {
        if (!userService.isExistLogin(user.getLogin()) && !userService.isExistEmail(user.getEmail())) {     //Дополнительная проверка на наличие юзера в базе
            String userLogin = user.getLogin();
            char[] userPass = userService.encodePassword(user.getPasswordChar());
            String userEmail = user.getEmail();
            UserRole userRole = userRoleService.findByAuthority(user.getAuthority().getAuthority());

            if (userRole.equals(roleSeeker)) {
                Seeker seeker = new Seeker(userLogin, userPass, userEmail, userRole, null);
                seekerService.add(seeker);
            } else if (userRole.equals(roleEmployer)) {
                Employer employer = new Employer(userLogin, userPass, userEmail, userRole, null);
                employerService.add(employer);
            }
        } else {
            throw new RuntimeException("User already exist");
        }
    }

}
