package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import static java.util.regex.Pattern.matches;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/employer/{string}")
    public List<User> getEmployerSearch(@PathVariable("string") String string) {
        final GrantedAuthority USER_AUTHORITY = new SimpleGrantedAuthority("ROLE_EMPLOYER");
        return buildListUsers(string, USER_AUTHORITY);
    }

    @GetMapping("/seeker/{string}")
    public List<User> getSeekerSearch(@PathVariable("string") String string) {
        final GrantedAuthority USER_AUTHORITY = new SimpleGrantedAuthority("ROLE_SEEKER");
        return buildListUsers(string, USER_AUTHORITY);
    }

    private List<User> buildListUsers(String string, GrantedAuthority USER_AUTHORITY) {
        List<User> users;
        if(emailVerification(string)) {
            users = userService.getUserByEmail(string);
            return createListUsers(users, USER_AUTHORITY);
        } else {
            if (USER_AUTHORITY.getAuthority().equals("ROLE_EMPLOYER")) {
                users = userService.getEmployerUserByName(string);
            } else {
                users = userService.getSeekerUserByName(string);
            }
            return createListUsers(users, USER_AUTHORITY);
        }
    }

    private List<User> createListUsers(List<User> users, GrantedAuthority USER_AUTHORITY) {
        List<User> newListUsers = new ArrayList<>();
        if (users.size() != 0) {
            for (User user : users) {
                if (user.getAuthority().equals(USER_AUTHORITY)) {
                    newListUsers.add(user);
                }
            }
        }
        return newListUsers;
    }

    private boolean emailVerification(String string) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return matches(EMAIL_PATTERN, string);
    }

}



//package com.jm.jobseekerplatform.controller.rest;
//
//import com.jm.jobseekerplatform.model.users.EmployerUser;
//import com.jm.jobseekerplatform.model.users.User;
//import com.jm.jobseekerplatform.service.impl.users.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.ArrayList;
//import java.util.List;
//import static java.util.regex.Pattern.matches;
//
//@RestController
//@RequestMapping("/api/search")
//public class SearchRestController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/employer/{string}")
//    public List<User> getEmployerSearch(@PathVariable("string") String string) {
//        final GrantedAuthority USER_AUTHORITY = new SimpleGrantedAuthority("ROLE_EMPLOYER");
//        return buildListUsers(string, USER_AUTHORITY);
//    }
//
//    @GetMapping("/seeker/{string}")
//    public List<User> getSeekerSearch(@PathVariable("string") String string) {
//        final GrantedAuthority USER_AUTHORITY = new SimpleGrantedAuthority("ROLE_SEEKER");
//        return buildListUsers(string, USER_AUTHORITY);
//    }
//
//    private List<User> buildListUsers(String string, GrantedAuthority USER_AUTHORITY) {
//        if(emailVerification(string)) {
//            List<User> users = userService.getUserByEmail(string);
//            return createListUsers(users, USER_AUTHORITY);
//        } else {
//            List<User> users = userService.getEmployerUserByName(string);
//            return createListUsers(users, USER_AUTHORITY);
//        }
//    }
//
//    private List<User> createListUsers(List<User> users, GrantedAuthority USER_AUTHORITY) {
//        List<User> newListUsers = new ArrayList<>();
//        if (users.size() != 0) {
//            for (User user : users) {
//                if (user.getAuthority().equals(USER_AUTHORITY)) {
//                    newListUsers.add(user);
//                }
//            }
//        }
//        return newListUsers;
//    }
//
//    private boolean emailVerification(String string) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        return matches(EMAIL_PATTERN, string);
//    }
//
//}
