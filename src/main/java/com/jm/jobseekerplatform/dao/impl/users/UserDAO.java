package com.jm.jobseekerplatform.dao.impl.users;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.User;
import org.springframework.stereotype.Repository;

@Repository("userDAO")
public class UserDAO extends AbstractDAO<User> {

    public User findByEmail(String email) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :param", User.class)
                .setParameter("param", email)
                .getSingleResult();
    }

    public boolean isExistEmail(String email) {
        return (boolean) entityManager
                .createQuery("SELECT CASE WHEN EXISTS (SELECT r FROM User r WHERE r.email = :param) THEN true ELSE false END FROM User")
                .setParameter("param", email)
                .getSingleResult();
    }
    public User findByProfileId(Long id) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.profile.id = :param", User.class)
                .setParameter("param", id)
                .getSingleResult();
    }
}
