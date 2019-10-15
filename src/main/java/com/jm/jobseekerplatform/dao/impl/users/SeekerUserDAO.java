package com.jm.jobseekerplatform.dao.impl.users;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository("seekerDAO")
public class SeekerUserDAO extends AbstractDAO<SeekerUser> {

    public SeekerUser getByProfileId(Long seekerProfileId) {
        return entityManager.createQuery("select e from  SeekerUser e where e.profile.id = :seekerProfileId", SeekerUser.class)
                .setParameter("seekerProfileId", seekerProfileId).getSingleResult();
    }

    /*public List<SeekerUser> getByDate(LocalDateTime startDate, LocalDateTime endDate){
        Query query = entityManager.createQuery("SELECT distinct e FROM SeekerUser e where e.date BETWEEN :startDate AND :endDate", SeekerUser.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<SeekerUser> seekerUserList = query.getResultList();
        return seekerUserList;
    }*/
}
