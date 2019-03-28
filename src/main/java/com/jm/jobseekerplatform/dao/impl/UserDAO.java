package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.User;
import org.springframework.stereotype.Repository;

@Repository("userDAO")
public class UserDAO extends AbstractDAO<User> {

    public User findByLogin(String login) {
        return entityManager
                .createQuery("SELECT r FROM User As r WHERE r.login = :param", User.class)
                .setParameter("param", login)
                .getSingleResult();
    }
}
