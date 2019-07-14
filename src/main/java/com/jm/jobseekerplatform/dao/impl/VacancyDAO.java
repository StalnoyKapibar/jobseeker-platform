package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.VacancyPageDTO;
import com.jm.jobseekerplatform.model.Point;
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

    //language=SQL
    private final static String SQL_getAllByEmployerProfileId = "SELECT v FROM Vacancy v WHERE v.employerProfile.id = :param";

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

    public List<Point> getPointsNotInCity(String city) {
        String s = "select * from points as p inner join (select * from vacancies as vc where vc.city!=:city) as v on p.id=v.coordinates_id";
        return new ArrayList<>(entityManager.unwrap(Session.class).createSQLQuery(s)
                .setParameter("city", city)
                .addEntity(Point.class).getResultList());
    }

    public Page<Vacancy> getVacanciesInCity(String city, int limit, int page) {
        if (page != 0) {
            --page;
        }

        Query query = (Query) entityManager.createQuery("SELECT v FROM Vacancy v  WHERE v.city IN (:param)", Vacancy.class)
                .setParameter("param", city);

        double totalElements = query.getResultList().size();
        int totalPages = (int) Math.ceil(totalElements / (double) limit);
        query.setFirstResult(page * limit).setMaxResults(limit);

        return new VacancyPageDTO(query.getResultList(), totalPages);
    }

    public Page<Vacancy> getByTagsInCity(String city, Set<Tag> tags, int limit, int page) {

        String query = "select * from vacancies as vc inner join (select v.vacancy_id as r_id, count(v.tags_id) as count "
                + "from vacancies_tags as v where v.tags_id in (:param) group by v.vacancy_id ) as result "
                + "on vc.id=result.r_id where vc.city=:city order by result.count desc";

        return getByTagsAndCityWithQuery(query, city, tags, limit, page);

    }

    public Page<Vacancy> getByTagsNotInCity(String city, Set<Tag> tags, int limit, int page) {

        String query = "select * from vacancies as vc inner join (select v.vacancy_id as r_id, count(v.tags_id) as count "
                + "from vacancies_tags as v where v.tags_id in (:param) group by v.vacancy_id ) as result "
                + "on vc.id=result.r_id where vc.city!=:city order by result.count desc";

        return getByTagsAndCityWithQuery(query, city, tags, limit, page);
    }

    private Page<Vacancy> getByTagsAndCityWithQuery(String sqlQuery, String city, Set<Tag> tags, int limit, int page) {
        if (page != 0) {
            --page;
        }
        Set<Long> tagsId = new HashSet<>();
        for (Tag tag : tags) { tagsId.add(tag.getId()); }

        Query query = entityManager.unwrap(Session.class).createSQLQuery(sqlQuery).addEntity(Vacancy.class)
                .setParameter("param", tagsId).setParameter("city", city);

        int totalElements = query.getResultList().size();
        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));
        query.setFirstResult(page * limit).setMaxResults(limit);

        return new VacancyPageDTO(query.getResultList(), totalPages);
    }
}

