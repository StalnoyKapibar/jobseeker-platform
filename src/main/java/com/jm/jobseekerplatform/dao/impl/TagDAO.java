package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("tagDAO")
public class TagDAO extends AbstractDAO<Tag> {

    public Tag findByName(String name) {
        return entityManager
                .createQuery("SELECT t FROM Tag t WHERE t.name = :param", Tag.class)
                .setParameter("param", name)
                .getSingleResult();
    }

    /**
     * we get only verified/unverified tags
     */
    public List<Tag> getVerified(boolean verified) {
        return (List<Tag>) entityManager
                .createQuery("SELECT tag FROM Tag tag WHERE tag.verified = :verified", Tag.class)
                .setParameter("verified", verified)
                .getResultList();
    }
}
