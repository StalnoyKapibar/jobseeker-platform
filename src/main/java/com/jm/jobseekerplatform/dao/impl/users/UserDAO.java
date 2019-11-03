package com.jm.jobseekerplatform.dao.impl.users;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.User;
import org.springframework.stereotype.Repository;
import java.util.List;

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

    public List<User> getUserByEmailDAO(String email) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<User> getEmployerUserByNameDAO(String name) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.profile.companyName = :name", User.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<User> getSeekerUserByNameDAO(String name) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.profile.name = :name " +
                "OR u.profile.patronymic = :name OR u.profile.surname = :name", User.class)
                .setParameter("name", name)
                .getResultList();
    }

}
