package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerStatusNews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
@Transactional
public class SeekerStatusNewsDAO extends AbstractDAO<SeekerStatusNews> {

    public List<SeekerStatusNews> getAllSeekerStatusNewsDAO(SeekerProfile seekerProfile) {
        return entityManager.createQuery("SELECT s FROM SeekerStatusNews s WHERE s.seeker = :id", clazz)
				.setParameter("id", seekerProfile)
				.getResultList();
    }

    public SeekerStatusNews getSeekerStatusNewsDAO(News news, SeekerProfile seekerProfile) {

        SeekerStatusNews ssn = null;
        try {
            ssn = entityManager.createQuery("SELECT s FROM SeekerStatusNews s WHERE s.news = :newsId AND s.seeker = :id", clazz)
                    .setParameter("newsId", news)
                    .setParameter("id", seekerProfile)
                    .getSingleResult();
        } catch (NoResultException e) {
            return new SeekerStatusNews();
        }
        return ssn;
    }

}
