package com.jm.jobseekerplatform.dao.impl.users;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("seekerDAO")
public class SeekerUserDAO extends AbstractDAO<SeekerUser> {

    public SeekerUser getByProfileId(Long seekerProfileId) {
        return entityManager.createQuery(
                "select e from  SeekerUser e where e.profile.id = :seekerProfileId", SeekerUser.class)
                .setParameter("seekerProfileId", seekerProfileId).getSingleResult();
    }

}
