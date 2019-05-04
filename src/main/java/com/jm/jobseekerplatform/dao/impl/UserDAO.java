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

    public boolean isExistLogin(String login) {
        Long count = (Long) entityManager
                .createQuery("SELECT COUNT(r) FROM User As r WHERE r.login = :param")
                .setParameter("param", login)
                .getSingleResult();
        return count > 0;
    }

    public boolean isExistEmail(String email) {
        Long count = (Long) entityManager
                .createQuery("SELECT COUNT(r) FROM User As r WHERE r.email = :param")
                .setParameter("param", email)
                .getSingleResult();
        return count > 0;
    }
}
