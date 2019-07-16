package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /**
     * we get only verified/unverified tags
     */
    public List<Tag> getVerified(boolean verified) {
        return entityManager
                .createQuery("SELECT tag FROM Tag tag WHERE tag.verified = :verified", Tag.class)
                .setParameter("verified", verified)
                .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {

        List<String> queries = new ArrayList<>();
        queries.add("DELETE FROM vacancies_tags WHERE tags_id = :id");
        queries.add("DELETE FROM profile_tags WHERE tags_id = :id");
        queries.add("DELETE FROM tags WHERE id = :id");

        queries.forEach(
                query -> entityManager
                .createNativeQuery(query)
                .setParameter("id", id)
                .executeUpdate()
        );
    }

    public List<Tag> getTagsByNames(Set<String> tagsName) {

        List<Tag> findedTags = entityManager
                .createQuery("SELECT tag FROM Tag tag WHERE tag.name IN :tagsName", Tag.class)
                .setParameter("tagsName", tagsName)
                .getResultList();
        return findedTags;
    }
}
