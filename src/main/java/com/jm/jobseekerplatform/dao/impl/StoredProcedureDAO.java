package com.jm.jobseekerplatform.dao.impl;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class StoredProcedureDAO {
    @PersistenceContext
    protected EntityManager entityManager;

    public void createSortVacancyProcedure() {
        entityManager.createNativeQuery("DROP PROCEDURE IF EXISTS getSortedVacs").executeUpdate();
        entityManager.createNativeQuery("CREATE PROCEDURE `getSortedVacs`(IN seekerProfileId long, IN cityName varchar(30), IN limitFrom int, IN limitTo int) " +
                "BEGIN " +
                "select * ," +
                "(select count(*) from vacancies_tags vt join profile_tags pt where vt.vacancy_id=v.id and pt.tags_id=vt.tags_id and pt.seeker_profile_id=seekerProfileId ) as countTags, " +
                "(select count(*) from seeker_vacancy_record svr where svr.vacancy_id=v.id and svr.seeker_id=seekerProfileId) as countViews, " +
                "(select distance from city_distances cd join city c where cd.from_city_id=v.city_id and c.name=cityName and cd.to_city_id=c.id) as distance " +
                "from vacancies v " +
                "group by v.id " +
                "order by " +
                "distance asc, countTags desc, countViews desc " +
                "limit limitFrom, limitTo; " +
                "END").executeUpdate();
    }
}
