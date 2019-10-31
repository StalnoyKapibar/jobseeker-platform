package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.interfaces.SeekerStatusNewsDao;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerStatusNews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public abstract class SeekerStatusNewsDaoImpl implements SeekerStatusNewsDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<SeekerStatusNews> getAllSeekerStatusNewsDAO(SeekerProfile seekerProfile) {
        return entityManager.createQuery("SELECT s FROM SeekerStatusNews s " +
                "WHERE s.seeker = :id", SeekerStatusNews.class)
                .setParameter("id", seekerProfile)
                .getResultList();
    }

    public SeekerStatusNews getSeekerStatusNewsDAO(News news, SeekerProfile seekerProfile) {
        SeekerStatusNews ssn;
        try {
            ssn = entityManager.createQuery("SELECT s FROM SeekerStatusNews s WHERE s.news = :newsId " +
                    "AND s.seeker = :id", SeekerStatusNews.class)
                    .setParameter("newsId", news)
                    .setParameter("id", seekerProfile)
                    .getSingleResult();
        } catch (NoResultException e) {
            return new SeekerStatusNews();
        }
        return ssn;
    }
}
