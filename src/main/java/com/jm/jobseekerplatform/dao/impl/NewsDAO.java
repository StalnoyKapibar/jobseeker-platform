package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.News;
import org.springframework.stereotype.Repository;

@Repository("newsDAO")
public class NewsDAO extends AbstractDAO<News> {

    @Override
    public void deleteById(Long newsId) {
        entityManager.createQuery("delete from News where id =: id").setParameter("id", newsId).executeUpdate();
    }
}
