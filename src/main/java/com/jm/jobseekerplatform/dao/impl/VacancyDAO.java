package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository("vacancyDAO")
public class VacancyDAO extends AbstractDAO<Vacancy> {

    //language=SQL
    private final static String SQL_getAllByEmployerProfileId = "SELECT v FROM Vacancy v WHERE v.profileEmployer.id = :param"; //todo check

    public Set<Vacancy> getAllByTags(Set<Tag> tags, int limit) {
        Set<Vacancy> vacancies = new HashSet<>();
        vacancies.addAll(entityManager
                .createQuery("SELECT v FROM Vacancy v JOIN v.tags t WHERE t IN (:param)", Vacancy.class)
                .setParameter("param", tags)
                .setMaxResults(limit)
                .getResultList());
        return vacancies;
    }

    public Set<Vacancy> getAllByEmployerProfileId(Long id, int limit) {
        Set<Vacancy> vacancies = new HashSet<>();

        vacancies.addAll(entityManager
                .createQuery(SQL_getAllByEmployerProfileId, Vacancy.class)
                .setParameter("param", id)
                .setMaxResults(limit)
                .getResultList());

        return vacancies;
    }

    public Set<Vacancy> getAllByEmployerProfileId(Long id) {
        return getAllByEmployerProfileId(id, Integer.MAX_VALUE);
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