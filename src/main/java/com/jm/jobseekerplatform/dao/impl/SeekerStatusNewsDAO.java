package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerStatusNews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class SeekerStatusNewsDAO extends AbstractDAO<SeekerStatusNews> {

    public List<SeekerStatusNews> getAllSeekerStatusNewsDAO(SeekerProfile seekerProfile) {
        return entityManager.createQuery("SELECT s FROM SeekerStatusNews s WHERE s.seeker = :id", clazz)
				.setParameter("id", seekerProfile)
				.getResultList();
    }

    public SeekerStatusNews getSeekerStatusNewsDAO(News news) {
        return entityManager.createQuery("SELECT s FROM SeekerStatusNews s WHERE s.news = :newsId", clazz)
                .setParameter("newsId", news)
                .getSingleResult();
    }

}
