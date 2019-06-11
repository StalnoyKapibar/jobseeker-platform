package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

@Repository("vacancyDAO")
public class VacancyDAO extends AbstractDAO<Vacancy> {


    public List<Vacancy> getByTags(Set<Tag> tags, int limit) {
        List<Vacancy> vacancies = new ArrayList<>();
        Set<Long> tagsId = new HashSet<>();
        for (Tag tag:tags) { tagsId.add(tag.getId()); }

        String s = "select * from vacancies as vc inner join (select v.vacancy_id as r_id, count(v.tags_id) as count from vacancies_tags as v where v.tags_id in (:param) group by v.vacancy_id ) as result on vc.id=result.r_id order by result.count desc";
        vacancies.addAll(entityManager.createNativeQuery(s)
                .setParameter("param", tagsId).setMaxResults(limit).getResultList());

        return vacancies;
    }

    public int deletePermanentBlockVacancies() {
        int deletedCount = entityManager.createQuery("DELETE FROM Vacancy v WHERE v.state = 'BLOCK_PERMANENT'").executeUpdate();
        return deletedCount;
    }

    public int deleteExpiryBlockVacancies() {
        Date currentDate = new Date();
        int deletedCount = entityManager.createQuery("DELETE FROM Vacancy v WHERE v.expiryBlock <= :param")
                .setParameter("param", currentDate)
                .executeUpdate();
        return deletedCount;
    }
}

