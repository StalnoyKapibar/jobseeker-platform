package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("tagDAO")
public class TagDAO extends AbstractDAO<Tag> {

    public Tag findByName(String name) {
       Tag tag = null;
        try {
            tag =  entityManager
                    .createQuery("SELECT t FROM Tag t WHERE t.name = :param", Tag.class)
                    .setParameter("param", name)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tag;
    }

    public List<Tag> getBySearchParam(String param) {
        String param1 = "%" + param + "%";
        return entityManager
                .createQuery("SELECT t FROM Tag t WHERE t.name like :param", Tag.class)
                .setParameter("param", param1)
                .getResultList();
    }
}
