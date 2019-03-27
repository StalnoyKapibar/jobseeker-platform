package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("userDAO")
public class UserDAO extends AbstractDAO<User> {

    @PersistenceContext
    private EntityManager entityManager;

    public User findByLogin(String login) {
        return entityManager
                .createQuery("SELECT r FROM User As r WHERE r.login = :param", User.class)
                .setParameter("param", login)
                .getSingleResult();
    }
}
