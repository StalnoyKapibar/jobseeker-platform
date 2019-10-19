package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerCountNewsViews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class SeekerCountDAO extends AbstractDAO<SeekerCountNewsViews> {

    public List<SeekerCountNewsViews> getAllSeekerCountDAO(SeekerProfile seekerProfile) {
        return entityManager.createQuery("SELECT s FROM SeekerCountNewsViews s WHERE s.seeker = :id", clazz).setParameter("id", seekerProfile).getResultList();
    }

    public SeekerCountNewsViews getSeekerCountDAO(News news) {
        return entityManager.createQuery("SELECT s FROM SeekerCountNewsViews s WHERE s.news = :newsId", clazz)
                .setParameter("newsId", news)
                .getSingleResult();
    }

}
