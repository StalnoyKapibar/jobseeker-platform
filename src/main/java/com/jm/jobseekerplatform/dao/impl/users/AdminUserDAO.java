package com.jm.jobseekerplatform.dao.impl.users;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.AdminUser;
import org.springframework.stereotype.Repository;

@Repository("adminDAO")
public class AdminUserDAO extends AbstractDAO<AdminUser> {

    public AdminUser getByProfileId(Long adminProfileId) {
        return entityManager.createQuery("select a from AdminUser a where a.profile.id =: adminProfileId", AdminUser.class)
                .setParameter("adminProfileId", adminProfileId).getSingleResult();
    }
}
