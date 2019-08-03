package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.VacancyPageDTO;
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
    private final static String query_for_find_vacancies_sorted_by_city =
            "select * from vacancies inner join (select result3.distance, city.id as sort_id, city.name from (select distances.distance, distances.city_id from " +
            "(select city.name, dist.city_distances_id from city inner join city_city_distances as dist where city.id=dist.city_id and city.name=:city) as result2 " +
            "inner join distances on result2.city_distances_id=distances.id) as result3 inner join city on result3.city_id=city.id order by result3.distance) as sort_dist " +
            "on vacancies.city_id=sort_dist.sort_id where vacancies.state='ACCESS' order by sort_dist.distance";

    //language=SQL
    private final static String query_for_find_vacancies_by_tags_and_sorted_by_city =
            "select * from (select * from vacancies as vc inner join (select v.vacancy_id as r_id, count(v.tags_id) as count " +
            "from vacancies_tags as v where v.tags_id in (:tags) group by v.vacancy_id ) as result on vc.id=result.r_id order by result.count desc) as result_vac " +
            "inner join (select result3.distance, city.id as sort_id, city.name from (select distances.distance, distances.city_id from " +
            "(select city.name, dist.city_distances_id from city inner join city_city_distances as dist where city.id=dist.city_id and city.name=:city) as result2 " +
            "inner join distances on result2.city_distances_id=distances.id) as result3 inner join city on result3.city_id=city.id order by result3.distance) as sort_dist " +
            "on result_vac.city_id=sort_dist.sort_id where result_vac.state='ACCESS' order by result_vac.count desc, sort_dist.distance";

    //language=SQL
    private final static String SQL_getAllByEmployerProfileId = "SELECT v FROM Vacancy v WHERE v.employerProfile.id = :param";

    public Set<Vacancy> getAllTracked(Long id){
        Set <Vacancy> vacancies = new HashSet<>();
        vacancies.addAll(entityManager.createQuery("select v from Vacancy v join v.employerProfile.id empl where empl = :id", Vacancy.class)
                .setParameter("id", id)
//                .setParameter("tracked", true)
        .getResultList());

        return vacancies;
    }

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
        if (page != 0) {
            --page;
        }

        Query query = entityManager.unwrap(Session.class).createSQLQuery(query_for_find_vacancies_sorted_by_city)
                .addEntity(Vacancy.class)
                .setParameter("city", city);

        int totalElements = query.getResultList().size();
        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));
        query.setFirstResult(page * limit).setMaxResults(limit);

        return new VacancyPageDTO(query.getResultList(), totalPages);

    }

    public Page<Vacancy> getVacanciesByTagsAndSortByCity(String city, Set<Tag> tags, int limit, int page) {
        if (page != 0) {
            --page;
        }

        Set<Long> tagsId = new HashSet<>();
        for (Tag tag : tags) { tagsId.add(tag.getId()); }

        Query query = entityManager.unwrap(Session.class).createSQLQuery(query_for_find_vacancies_by_tags_and_sorted_by_city)
                .addEntity(Vacancy.class)
                .setParameter("tags", tagsId)
                .setParameter("city", city);

        int totalElements = query.getResultList().size();
        int totalPages = (int) (Math.ceil((double) totalElements / (double) limit));
        query.setFirstResult(page * limit).setMaxResults(limit);

        return new VacancyPageDTO(query.getResultList(), totalPages);
    }
}

