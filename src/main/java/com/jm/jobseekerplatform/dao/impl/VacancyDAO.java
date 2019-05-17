package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("vacancyDAO")
public class VacancyDAO extends AbstractDAO<Vacancy> {

    public Set<Vacancy> getByTags(Set<Tag> tags, int limit) {
        Set<Vacancy> vacancies = new HashSet<>();
        vacancies.addAll(entityManager
                .createQuery("SELECT v FROM Vacancy v JOIN v.tags t WHERE t IN (:param)", Vacancy.class)
                .setParameter("param", tags)
                .setMaxResults(limit)
                .getResultList());
        return vacancies;
    }

    public Map<Tag, List<Vacancy>> getMapVacancyByTags(Set<Tag> tags, int limit) {
        Map<Tag, List<Vacancy>> vacancyMap = new HashMap<>();
        if (tags != null && !tags.isEmpty()) {
            tags.forEach(tag -> vacancyMap.put(tag, vacancyByTag(tag, limit)));
        }
        return vacancyMap;
    }

    private List<Vacancy> vacancyByTag(Tag tag, int limit) {
        return entityManager.createQuery("select v from  Vacancy v join v.tags t where t in (:tag)", Vacancy.class)
                .setParameter("tag", tag)
                .setMaxResults(limit)
                .getResultList();
    }
}