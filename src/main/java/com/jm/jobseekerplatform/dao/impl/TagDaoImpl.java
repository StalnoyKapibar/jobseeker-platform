package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.interfaces.TagDao;
import com.jm.jobseekerplatform.model.Tag;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Tag> getBySearchParam(String param) {
        String param1 = "%" + param + "%";
        return entityManager
                .createQuery("SELECT t FROM Tag t WHERE t.name like :param", Tag.class)
                .setParameter("param", param1)
                .getResultList();
    }
}
