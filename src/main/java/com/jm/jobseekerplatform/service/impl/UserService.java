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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Pattern pattern;
    private Matcher matcher;

    public User findByLogin(String login) {
        return dao.findByLogin(login);
    }

    public char[] encodePassword(char[] password) {
        return passwordEncoder.encode(String.valueOf(password)).toCharArray();
    }

    public boolean isExistLogin(String login) {
        return dao.isExistLogin(login);
    }

    public void registerNewUser (User user) {
        String userLogin = user.getLogin();
        char[] userPass = encodePassword(user.getPasswordChar());
        UserRole userRole = userRoleService.findByAuthority(user.getAuthority().getAuthority());

        if (userRole.equals(roleSeeker)) {
            Seeker seeker = new Seeker(userLogin, userPass, userRole, null);
            seekerService.add(seeker);
        } else if (userRole.equals(roleEmployer)) {
            Employer employer = new Employer(userLogin, userPass, userRole, null);
            employerService.add(employer);
        }

        User registeredUser = findByLogin(userLogin);
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(token, registeredUser);
        mailService.sendVerificationEmail(userLogin, token);
    }

    public boolean validateNewUser(User user) {
        UserRole userRole = user.getAuthority();
        String email_pattern = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$";
        String pass_pattern = "^(?=.*[A-Z].*)(?=.*[a-z].*)(?=.*[0-9].*)[A-Za-z0-9]{8,20}$";
        boolean isCorrect;

        if (user.getLogin().isEmpty() || user.getPassword().isEmpty() || user.getAuthority().getAuthority().isEmpty()) {
            throw new IllegalArgumentException("Some fields is empty");
        }
        if (isExistLogin(user.getLogin())) {
            throw new IllegalArgumentException("User's login already exist");
        }

        isCorrect = (userRole.equals(roleSeeker) | userRole.equals(roleEmployer));

        pattern = Pattern.compile(email_pattern);
        matcher = pattern.matcher(user.getLogin());
        isCorrect &= matcher.matches();

        pattern = Pattern.compile(pass_pattern);
        matcher = pattern.matcher(user.getPassword());
        isCorrect &= matcher.matches();

        if (!isCorrect) {
            throw new IllegalArgumentException("Some fields is incorrect");
        }

        return isCorrect;
        }
}
