package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ViewedVacanciesDTO;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
public class SeekerVacancyRecordDAO extends AbstractDAO<SeekerVacancyRecord> {

    @Autowired
    VacancyService vacancyService;


    //language=SQL
    private final static String query_for_get_viewed_vacancies =
            "select distinct v.id, v.headline, IFNULL(v.salarymin, 0) as salarymin, " +
            "IFNULL(v.salarymax, 0) as salarymax, p.companyname  from seeker_vacancy_record as s left join vacancies as v " +
            "on s.vacancy_id=v.id  left join profile p on v.creator_profile_id=p.id where s.seeker_id=:id";

    private final static String query_for_get_all_viewed_vacancies =
            "select v.id, v.headline, IFNULL(v.salarymin, 0) as salarymin, " +
                    "IFNULL(v.salarymax, 0) as salarymax, p.companyname  from seeker_vacancy_record as s left join vacancies as v " +
                    "on s.vacancy_id=v.id  left join profile p on v.creator_profile_id=p.id where s.seeker_id=:id";


    public List<ViewedVacanciesDTO> getViewedVacanciesBySeeker(Long seekerId) {
        List<ViewedVacanciesDTO> listVacancies = new ArrayList();
        List vacancies = entityManager
                .createNativeQuery(query_for_get_viewed_vacancies)
                .setParameter("id", seekerId).getResultList();

        for (Object vacancy : vacancies) {
            ViewedVacanciesDTO dto = new ViewedVacanciesDTO();
            dto.setId(Long.parseLong(((Object[]) vacancy)[0].toString()));
            dto.setHeadline((String)((Object[]) vacancy)[1]);
            dto.setSalarymin(Integer.parseInt(((Object[]) vacancy)[2].toString()));
            dto.setSalarymax(Integer.parseInt(((Object[]) vacancy)[3].toString()));
            dto.setCompanyname((String)((Object[]) vacancy)[4]);
            listVacancies.add(dto);
        }
        return listVacancies;
    }

    public HashMap<Vacancy, Long> getNumberOfViewsOffAllVacanciesByTagForSeeker(SeekerProfile seekerProfile) {
        Long profileId = seekerProfile.getId();
        Set<Tag> profileTags = seekerProfile.getTags();
        HashMap<Vacancy, Long> numberOfViewsVacanciesByTag = new HashMap<>();

        List<ViewedVacanciesDTO> vacanciesDTOList = new ArrayList();
        List vacancies = entityManager
                .createNativeQuery(query_for_get_all_viewed_vacancies)
                .setParameter("id", profileId).getResultList();

        for (Object vacancy : vacancies) {
            ViewedVacanciesDTO dto = new ViewedVacanciesDTO();
            dto.setId(Long.parseLong(((Object[]) vacancy)[0].toString()));
            dto.setHeadline((String)((Object[]) vacancy)[1]);
            dto.setSalarymin(Integer.parseInt(((Object[]) vacancy)[2].toString()));
            dto.setSalarymax(Integer.parseInt(((Object[]) vacancy)[3].toString()));
            dto.setCompanyname((String)((Object[]) vacancy)[4]);
            vacanciesDTOList.add(dto);
        }

        List<Vacancy> vacancyList = vacancyService.getAll();
        HashMap<Long, Vacancy> vacancyHashMap = new HashMap<>();
        for (Vacancy vacancy : vacancyList) {
            vacancyHashMap.put(vacancy.getId(), vacancy);
        }

        for (ViewedVacanciesDTO vacanciesDTO : vacanciesDTOList) {
            Vacancy vacancy = vacancyHashMap.get(vacanciesDTO.getId());
            Set<Tag> tagsVacancies = vacancy.getTags();
            for (Tag tag : tagsVacancies) {
                if (profileTags.contains(tag)) {
                    if (numberOfViewsVacanciesByTag.containsKey(vacancy)) {
                        Long aLong = numberOfViewsVacanciesByTag.get(vacancy);
                        numberOfViewsVacanciesByTag.replace(vacancy, ++aLong);
                        break;
                    } else {
                        numberOfViewsVacanciesByTag.put(vacancy, 1L);
                        break;
                    }
                }
            }
        }
        return numberOfViewsVacanciesByTag;
    }
}
