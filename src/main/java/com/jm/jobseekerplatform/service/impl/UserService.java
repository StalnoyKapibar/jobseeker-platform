package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.UserDAO;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserService extends AbstractService<User> {

    @Autowired
    @Qualifier("userDAO")
    private UserDAO dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByLogin(String login) {
        return dao.findByLogin(login);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
