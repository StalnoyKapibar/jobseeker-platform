package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.User;
import org.springframework.stereotype.Repository;

@Repository("userDAO")
public class UserDAO extends AbstractDAO<User> {

    public User findByName(String name) {
        return entityManager
                .createQuery("SELECT r FROM User As r WHERE r.name = :param", User.class)
                .setParameter("param", name)
                .getSingleResult();
    }
}
