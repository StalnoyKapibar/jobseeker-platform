package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.PageVacancyDTO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.*;

@SuppressWarnings("unchecked")
@Repository("vacancyDAO")
public class VacancyDAO extends AbstractDAO<Vacancy> {

    public Page<Vacancy> getByTags(Set<Tag> tags, int limit, int page) {
        Set<Long> tagsId = new HashSet<>();
        for (Tag tag:tags) { tagsId.add(tag.getId()); }

        String s = "select * from vacancies as vc inner join (select v.vacancy_id as r_id, count(v.tags_id) as count "
                +"from vacancies_tags as v where v.tags_id in (:param) group by v.vacancy_id ) as result "
                +"on vc.id=result.r_id order by result.count desc";

        Query query = entityManager.unwrap(Session.class).createSQLQuery(s).addEntity(Vacancy.class)
                .setParameter("param", tagsId);

        int totalElements = query.getResultList().size();
        int totalPages = (int) (Math.ceil(totalElements / limit));

        query.setFirstResult(page * limit);
        query.setMaxResults(limit);

        return new PageVacancyDTO(query.getResultList(), totalPages);
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

