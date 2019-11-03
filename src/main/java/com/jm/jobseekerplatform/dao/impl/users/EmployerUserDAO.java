package com.jm.jobseekerplatform.dao.impl.users;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("employerDAO")
public class EmployerUserDAO extends AbstractDAO<EmployerUser> {

    public EmployerUser getByProfileId(Long employerProfileId) {
        return entityManager.createQuery(
                "select e from  EmployerUser e where e.profile.id = :employerProfileId", EmployerUser.class)
                .setParameter("employerProfileId", employerProfileId).getSingleResult();
    }

}
