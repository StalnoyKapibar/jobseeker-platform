package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.User;
import org.springframework.stereotype.Repository;

@Repository("userDAO")
public class UserDAO extends AbstractDAO<User> {

    public User findByLogin(String login) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.login = :param", User.class)
                .setParameter("param", login)
                .getSingleResult();
    }

    public boolean isExistLogin(String login) {
        return (boolean) entityManager
                .createQuery("SELECT CASE WHEN EXISTS (SELECT r FROM User r WHERE r.login = :param) THEN true ELSE false END FROM User")
                .setParameter("param", login)
                .getSingleResult();
    }
}
