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

    /**
     * Процедура в БД для удаления всех связанных с User и его Profile записей
     */
    public void createDeleteUserProcedure() {
        entityManager.createNativeQuery("DROP PROCEDURE IF EXISTS deleteUser").executeUpdate();
        entityManager.createNativeQuery("CREATE PROCEDURE `deleteUser`(IN profileId long, IN userId long) " +
                "BEGIN " +
                "DELETE FROM `verification_token` WHERE user_id = userId; " +
                "DELETE FROM `password_reset_token` WHERE user_id = userId; " +
                "DELETE FROM `chats_chat_members` WHERE chat_members_id = userId; " +
                "DELETE FROM `profile_portfolios` WHERE seeker_profile_id = profileId; " +
                "DELETE `po`.* FROM `portfolios` `po` " +
                "    LEFT JOIN `profile_portfolios` `pr` ON `po`.id = `pr`.portfolios_id " +
                "WHERE `pr`.portfolios_id IS NULL; " +
                "DELETE FROM `employer_reviews` WHERE creator_profile_id = profileId OR employer_id = profileId; " +
                "DELETE `cmes`.*, `cmem`.*  FROM chats " +
                "    LEFT JOIN `chats_chat_messages` `cmes` ON chats.id = `cmes`.chat_id " +
                "    LEFT JOIN `chats_chat_members` `cmem` ON chats.id = `cmem`.chat_id " +
                "WHERE chats.creator_profile_id = profileId; " +
                "DELETE FROM chats WHERE creator_profile_id = profileId; " +
                "DELETE `cmes`.*, `chprid`.*  FROM `chatmessages` `cm` " +
                "    LEFT JOIN `chats_chat_messages` `cmes` ON `cm`.id = `cmes`.chat_messages_id " +
                "    LEFT JOIN `chat_message_is_read_by_profiles_id`  `chprid` ON `cm`.id = `chprid`.chat_message_id " +
                "WHERE `cm`.creator_profile_id = profileId; " +
                "DELETE FROM `chatmessages` WHERE creator_profile_id = profileId; " +
                "DELETE FROM `profile_subscriptions` WHERE seeker_profile_id = profileId; " +
                "DELETE `ps`.*, `st`.* FROM `subscription` `sb` " +
                "    LEFT JOIN `profile_subscriptions` `ps` ON `sb`.id = `ps`.subscriptions_id " +
                "    LEFT JOIN `subscription_tags` `st` ON `sb`.id = `st`.subscription_id " +
                "WHERE `sb`.seeker_profile_id = profileId OR `sb`.employer_profile_id = profileId; " +
                "DELETE FROM `subscription` `sb` " +
                "    WHERE `sb`.seeker_profile_id = profileId OR `sb`.employer_profile_id = profileId; " +
                "DELETE `nt`.* FROM `news` " +
                "    LEFT JOIN  `news_tags` `nt`  ON `news`.id = `nt`.news_id " +
                "WHERE `news`.employer_profile_id = profileId; " +
                "DELETE FROM `news` WHERE employer_profile_id = profileId; " +
                "DELETE FROM `profile_tags` WHERE seeker_profile_id = profileId; " +
                "DELETE FROM `profile_favorite_vacancy` WHERE seeker_profile_id = profileId; " +
                "DELETE FROM `seeker_vacancy_record` WHERE seeker_id = profileId; " +
                "DELETE `mg`.*, `svr`.*, `pfv`.*, `vt`.* FROM `vacancies` `vs` " +
                "    LEFT JOIN `vacancies_tags` `vt`  ON `vs`.id = `vt`.vacancy_id " +
                "    LEFT JOIN `profile_favorite_vacancy` `pfv` ON `vs`.id = `pfv`.favorite_vacancy_id " +
                "    LEFT JOIN `seeker_vacancy_record` `svr`  ON `vs`.id = `svr`.vacancy_id " +
                "    LEFT JOIN `meeting` `mg`  ON `vs`.id = `mg`.vacancy_id " +
                "WHERE `vs`.creator_profile_id = profileId; " +
                "DELETE FROM `vacancies` WHERE creator_profile_id = profileId; " +
                "DELETE `je`.*, `rje`.*, `rt`.*, `pr`.* FROM `resumes` `rs`  " +
                "    LEFT JOIN `resumes_tags` `rt` ON `rs`.id = `rt`.resume_id " +
                "    LEFT JOIN `profile_resumes` `pr` ON `rs`.id = `pr`.resumes_id " +
                "    LEFT JOIN `resumes_job_experiences` `rje` ON `rs`.id = `rje`.resume_id " +
                "    LEFT JOIN `job_experience` `je` ON `rje`.job_experiences_id = `je`.id " +
                "WHERE `rs`.creator_profile_id = profileId; " +
                "DELETE FROM `meeting` WHERE seeker_profile_id = profileId; " +
                "DELETE FROM `profile_resumes` WHERE seeker_profile_id = profileId; " +
                "DELETE FROM `resumes` WHERE creator_profile_id = profileId; " +
                "DELETE FROM `review_vote` WHERE seeker_profile_id = profileId OR employer_profile_id = profileId; " +
                "DELETE FROM `users` WHERE id = userId; " +
                "DELETE FROM `profile` WHERE id = profileId; " +
                "END").executeUpdate();
    }
}
