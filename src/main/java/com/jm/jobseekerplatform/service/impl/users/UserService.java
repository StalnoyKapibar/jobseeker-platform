package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.impl.users.UserDAO;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.profiles.AdminProfile;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.AdminUser;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.AbstractService;
import com.jm.jobseekerplatform.service.impl.ImageService;
import com.jm.jobseekerplatform.service.impl.MailService;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.VerificationTokenService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;
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
    private MailService mailService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProfileService profileService;

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
            SeekerProfile seekerProfile = (SeekerProfile) getDefaultProfile(userRole.getAuthority());
            profileService.add(seekerProfile);
            userNew = new SeekerUser(userEmail, userPass, LocalDateTime.now(), userRole, seekerProfile);
        } else if (userRole.equals(roleEmployer)) {
            EmployerProfile employerProfile = (EmployerProfile) getDefaultProfile(userRole.getAuthority());
            profileService.add(employerProfile);
            userNew = new EmployerUser(userEmail, userPass, LocalDateTime.now(), userRole, employerProfile);
        }
        add(userNew);
        //так нужно сделать
        User registeredUser = findByEmail(userEmail);
        String token = UUID.randomUUID().toString();
        //так нкжно сделать
        verificationTokenService.createVerificationToken(token, registeredUser);
        mailService.sendVerificationEmail(userEmail, token);
    }

    public void addNewUserByAdmin(User user, boolean check) {
        String userEmail = user.getEmail();
        char[] userPass = encodePassword(user.getPasswordChar());
        UserRole userRole = userRoleService.findByAuthority(user.getAuthority().getAuthority());
        User newUser = null;
        if (userRole.equals(roleSeeker)) {
            SeekerProfile seekerProfile = (SeekerProfile) getDefaultProfile(userRole.getAuthority());
            profileService.add(seekerProfile);
            newUser = new SeekerUser(userEmail, userPass, LocalDateTime.now(), userRole, seekerProfile);
        } else if (userRole.equals(roleEmployer)) {
            EmployerProfile employerProfile = (EmployerProfile) getDefaultProfile(userRole.getAuthority());
            profileService.add(employerProfile);
            newUser = new EmployerUser(userEmail, userPass, LocalDateTime.now(), userRole, employerProfile);
        } else if (userRole.equals(roleAdmin)) {
            AdminProfile adminProfile = (AdminProfile) getDefaultProfile(userRole.getAuthority());
            profileService.add(adminProfile);
            newUser = new AdminUser(userEmail, userPass, LocalDateTime.now(), userRole, adminProfile);
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

    public void inviteFriend(String user, String friend) {
        mailService.sendFriendInvitaionEmail(user, friend);
    }

    private Profile getDefaultProfile(String typeProfile) {
        typeProfile = typeProfile.toLowerCase();
        if (typeProfile.contains("seeker")) {
            return new SeekerProfile();
        } else if (typeProfile.contains("employer")) {
            return new EmployerProfile();
        } else if (typeProfile.contains("admin")) {
            return new AdminProfile();
        }
        return new Profile();
    }
}

