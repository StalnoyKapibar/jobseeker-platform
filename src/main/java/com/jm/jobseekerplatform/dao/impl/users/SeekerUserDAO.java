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

    public List<SeekerUser> getSeekerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return entityManager.createQuery(
                "SELECT distinct e FROM SeekerUser e where e.date between :startDate and :endDate", SeekerUser.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}
