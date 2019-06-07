package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;

@Repository("tagDAO")
public class TagDAO extends AbstractDAO<Tag> {

    public Tag findByName(String name) {
        return entityManager
                .createQuery("SELECT t FROM Tag t WHERE t.name = :param", Tag.class)
                .setParameter("param", name)
                .getSingleResult();
    }
}
