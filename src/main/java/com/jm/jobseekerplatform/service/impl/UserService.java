package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.UserDAO;
import com.jm.jobseekerplatform.model.Employer;
import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service("userService")
@Transactional
public class UserService extends AbstractService<User> {

    @Autowired
    private UserDAO dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private MailService mailService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");
    private UserRole roleEmployer = new UserRole("ROLE_EMPLOYER");

    public User findByLogin(String login) {
        return dao.findByLogin(login);
    }

    public char[] encodePassword(char[] password) {
        return passwordEncoder.encode(String.valueOf(password)).toCharArray();
    }

    public boolean isExistLogin(String login) {
        return dao.isExistLogin(login);
    }

    public boolean isExistEmail(String email) {
        return dao.isExistEmail(email);
    }

    public void registerNewUser (User user) {
        String userLogin = user.getLogin();
        char[] userPass = encodePassword(user.getPasswordChar());
        String userEmail = user.getEmail();
        UserRole userRole = userRoleService.findByAuthority(user.getAuthority().getAuthority());

        if (userRole.equals(roleSeeker)) {
            Seeker seeker = new Seeker(userLogin, userPass, userEmail, userRole, null);
            seekerService.add(seeker);
        } else if (userRole.equals(roleEmployer)) {
            Employer employer = new Employer(userLogin, userPass, userEmail, userRole, null);
            employerService.add(employer);
        }

        User registeredUser = findByLogin(userLogin);
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(token, registeredUser);
        mailService.sendVerificationEmail(userEmail, token);
    }

    public boolean validateNewUser(User user) {
        if (user.getLogin().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getAuthority().getAuthority().isEmpty()) {
            throw new IllegalArgumentException("Some fields is empty");
        }
        if (isExistLogin(user.getLogin()) || isExistEmail(user.getEmail())) {
            throw new IllegalArgumentException("User's login or email already exist");
        }
        if (false) { //add validate fields
            throw new IllegalArgumentException("Some fields is incorrect");
        }
        return true;
        }
}
