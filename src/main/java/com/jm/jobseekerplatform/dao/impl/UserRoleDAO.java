package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.UserRole;
import org.springframework.stereotype.Repository;

@Repository("userRoleDAO")
public class UserRoleDAO extends AbstractDAO<UserRole> {

    public UserRole findByAuthority(String authority) {
        return entityManager
                .createQuery("SELECT r FROM UserRole As r WHERE r.authority = :param", UserRole.class)
                .setParameter("param", authority)
                .getSingleResult();
    }
}
