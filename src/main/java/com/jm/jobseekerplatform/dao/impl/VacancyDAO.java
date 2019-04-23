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

    public Set<Vacancy> getByTags(Set<Tag> tags) {
        Set<Vacancy> vacancies = new HashSet<>();
        for (Tag tag : tags) {
            vacancies.addAll(entityManager
                    .createQuery("SELECT v FROM Vacancy v JOIN v.tags t WHERE t.id = :param", Vacancy.class)
                    .setParameter("param", tag.getId()).getResultList());
        }
        return vacancies;
    }
}