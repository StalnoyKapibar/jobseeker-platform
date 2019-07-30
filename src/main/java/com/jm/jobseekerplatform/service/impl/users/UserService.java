package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.impl.users.UserDAO;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.tokens.PasswordResetToken;
import com.jm.jobseekerplatform.model.tokens.VerificationToken;
import com.jm.jobseekerplatform.model.users.AdminUser;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.AbstractService;
import com.jm.jobseekerplatform.service.impl.MailService;
import com.jm.jobseekerplatform.service.impl.tokens.PasswordResetTokenService;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.tokens.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;

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
    private UserRoleService userRoleService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private MailService mailService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");
    private UserRole roleEmployer = new UserRole("ROLE_EMPLOYER");
    private UserRole roleAdmin = new UserRole("ROLE_ADMIN");

    private Pattern pattern;
    private Matcher matcher;

    public User findByEmail(String email) {
        return dao.findByEmail(email);
    }

    public char[] encodePassword(char[] password) {
        return passwordEncoder.encode(String.valueOf(password)).toCharArray();
    }

    public boolean isExistEmail(String email) {
        return dao.isExistEmail(email);
    }

    public void registerNewUser(User user) {
        String userEmail = user.getEmail();
        char[] userPass = encodePassword(user.getPasswordChar());
        UserRole userRole = userRoleService.findByAuthority(user.getAuthority().getAuthority());

        User userNew = null;

        if (userRole.equals(roleSeeker)) {
            userNew = new SeekerUser(userEmail, userPass, LocalDateTime.now(), userRole, null);
        } else if (userRole.equals(roleEmployer)) {
            userNew = new EmployerUser(userEmail, userPass, LocalDateTime.now(), userRole, null);
        }

        add(userNew);

        User registeredUser = findByEmail(userEmail);

        String token = UUID.randomUUID().toString();
//        VerificationToken verificationToken =
//                new VerificationToken(token, registeredUser, verificationTokenService.calculateExpiryDate());
//        verificationTokenService.add(verificationToken);
//        verificationTokenService.createToken(token,registeredUser);
        mailService.sendVerificationEmail(userEmail, token);
    }

    public void recoveryPassRequest(String email) {
        User recoveryUser = findByEmail(email);

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken =
                new PasswordResetToken(token, recoveryUser, passwordResetTokenService.calculateExpiryDate());
        passwordResetTokenService.add(passwordResetToken);
//        passwordResetTokenService.createToken(token,recoveryUser);
        mailService.sendRecoveryPassEmail(email, token);
    }

    public void passwordReset(String token, char[] password) {
        try {
            PasswordResetToken passwordResetToken= passwordResetTokenService.findByToken(token);
            boolean exists = passwordResetTokenService.tokenIsNonExpired(passwordResetToken);
            if (exists) {
                String email = passwordResetToken.getUser().getEmail();
                User newPassUser = findByEmail(email);
                char[] newPass = encodePassword(password);
                newPassUser.setPassword(newPass);
                update(newPassUser);
                passwordResetTokenService.delete(passwordResetToken);
            }
        } catch (NoResultException e) {
            e.printStackTrace();
        }

    }

    public void addNewUserByAdmin(User user, boolean check) {
        String userEmail = user.getEmail();
        char[] userPass = encodePassword(user.getPasswordChar());
        UserRole userRole = userRoleService.findByAuthority(user.getAuthority().getAuthority());
        User newUser = null;
        if (userRole.equals(roleSeeker)) {
            newUser = new SeekerUser(userEmail, userPass, LocalDateTime.now(), userRole, null);
        } else if (userRole.equals(roleEmployer)) {
            newUser = new EmployerUser(userEmail, userPass, LocalDateTime.now(), userRole, null);
        } else if (userRole.equals(roleAdmin)) {
            newUser = new AdminUser(userEmail, userPass, LocalDateTime.now(), userRole, null);
        }

        newUser.setConfirm(true);
        add(newUser);

        if (check) {
            mailService.sendNotificationAboutAddEmail(userEmail, user.getPassword());
        }
    }

    public boolean validateNewUser(User user) {
        UserRole userRole = user.getAuthority();
        String email_pattern = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$";
        String pass_pattern = "^(?=.*[a-z].*)(?=.*[0-9].*)[A-Za-z0-9]{6,20}$";
        boolean isCorrect;

        if (user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getAuthority().getAuthority().isEmpty()) {
            throw new IllegalArgumentException("Some fields is empty");
        }
        if (isExistEmail(user.getEmail())) {
            throw new IllegalArgumentException("User's email already exist");
        }

        isCorrect = (userRole.equals(roleSeeker) | userRole.equals(roleEmployer) | userRole.equals(roleAdmin));

        pattern = Pattern.compile(email_pattern);
        matcher = pattern.matcher(user.getEmail());
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
