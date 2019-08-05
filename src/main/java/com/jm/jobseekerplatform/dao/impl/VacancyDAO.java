package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.VacancyPageDTO;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;

import org.hibernate.annotations.QueryHints;
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
    private final static String query_for_find_vacancies_sorted_by_city =
            "select v from Vacancy v join v.city c join CityDistance cd on c=cd.to where cd.from.name=:city and v.state='ACCESS' order by cd.distance";

    //language=SQL
    private final static String query_for_find_vacancies_by_tags_and_sorted_by_city =
            "select v.id, max(cd.distance) as m from Vacancy v join v.tags t  join v.city c join CityDistance as cd on c=cd.to where cd.from.name=:city and v.state='ACCESS' and t in (:tags) group by (v.id) order by count (v.id) desc, m asc";

    //language=SQL
    private final static String query_for_find_vacancies_by_tags =
            "select v.id, count(v.id) as c from Vacancy v join v.tags t where v.state='ACCESS' and t in (:tags) group by (v.id) order by c desc";

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

    public Page<Vacancy> getVacanciesSortByCity(String city, int limit, int page) {
        page = (page==0) ? page : --page;

        Query query = (Query) entityManager.createQuery(query_for_find_vacancies_sorted_by_city, Vacancy.class);

        long totalElements = (Long)entityManager.createQuery("select count(v) from Vacancy v where v.state='ACCESS'")
                .setHint("org.hibernate.cacheable", true).getSingleResult();

        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));
        List<Vacancy> vacancies = query.setFirstResult(page * limit).setMaxResults(limit).setParameter("city", city)
                .setHint(QueryHints.FETCHGRAPH, entityManager.getEntityGraph("vacancy-all-nodes")).getResultList();

        return new VacancyPageDTO(vacancies, totalPages);
    }

    public Page<Vacancy> getVacanciesByTagsAndSortByCity(String city, Set<Tag> tags, int limit, int page) {
        page = (page==0) ? page : --page;

        Query query = (Query) entityManager.createQuery(query_for_find_vacancies_by_tags_and_sorted_by_city)
                .setParameter("tags", tags).setParameter("city", city);
        return getVacancies(query, tags, limit, page);
    }

    public Page<Vacancy> getVacanciesByTag(Set<Tag> tags, int limit, int page) {
        Query query = (Query) entityManager.createQuery(query_for_find_vacancies_by_tags)
                .setParameter("tags", tags);
        return getVacancies(query, tags, limit, page);
    }

    private Page<Vacancy> getVacancies(Query query, Set<Tag> tags, int limit, int page) {
        long totalElements = (long) entityManager
                .createQuery("select count(distinct v) from Vacancy v join v.tags t where v.state='ACCESS' and t in (:tags)")
                .setParameter("tags", tags).getSingleResult();

        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));

        List ids = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();
        List<Long> vacancyIds = new ArrayList<>();
        for (Object vacancy : ids) {
            vacancyIds.add((Long) ((Object[]) vacancy)[0]);
        }

        List<Vacancy> vacancyList = entityManager.createQuery("select v from Vacancy v where v.id in (:ids)").setParameter("ids", vacancyIds)
                .setHint(QueryHints.FETCHGRAPH, entityManager.getEntityGraph("vacancy-all-nodes")).getResultList();

        List<Vacancy> vacancies = new ArrayList<>();
        for (Long id : vacancyIds) {
            for(Vacancy v : vacancyList) {
                if(v.getId().equals(id)) {
                    vacancies.add(v);
                }
            }
        }
        return new VacancyPageDTO(vacancies, totalPages);
    }

    @Override
    public Vacancy getById(Long id) {
        return entityManager.createQuery("select v from Vacancy v where v.id=:id", Vacancy.class).setParameter("id", id)
                .setHint(QueryHints.FETCHGRAPH, entityManager.getEntityGraph("vacancy-all-nodes")).getSingleResult();
    }

    @Override
    public List<Vacancy> getAll() {
        return entityManager.createQuery("select v from Vacancy v", Vacancy.class)
                .setHint(QueryHints.FETCHGRAPH, entityManager.getEntityGraph("vacancy-all-nodes")).getResultList();
    }
}

