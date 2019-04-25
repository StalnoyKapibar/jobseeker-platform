package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}