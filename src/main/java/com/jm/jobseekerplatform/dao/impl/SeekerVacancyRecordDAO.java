package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ViewedVacanciesDTO;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
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
        List vacancies = entityManager
                .createNativeQuery(query_for_get_viewed_vacancies)
                .setParameter("id", seekerId).getResultList();
        List<ViewedVacanciesDTO> vacanciesDTOList = new ArrayList<>();
        for (Object vacancy : vacancies) {
            ViewedVacanciesDTO dto = new ViewedVacanciesDTO();
            dto.setId(Long.parseLong(((Object[]) vacancy)[0].toString()));
            dto.setHeadline((String) ((Object[]) vacancy)[1]);
            dto.setSalarymin(Integer.parseInt(((Object[]) vacancy)[2].toString()));
            dto.setSalarymax(Integer.parseInt(((Object[]) vacancy)[3].toString()));
            dto.setCompanyname((String) ((Object[]) vacancy)[4]);
            vacanciesDTOList.add(dto);
        }
        return vacanciesDTOList;
    }

    public List<ViewedVacanciesDTO> getNumberOfViewsOffAllVacanciesByTagForSeeker(SeekerProfile seekerProfile) {
        Long profileId = seekerProfile.getId();
        List<ViewedVacanciesDTO> vacanciesDTOList = new ArrayList();
        List vacs = entityManager.createNativeQuery("select v.id, v.headline, IFNULL(v.salarymin, 0) as salarymin, " +
                "IFNULL(v.salarymax, 0) as salarymax, p.companyname,  (select count(*) from seeker_vacancy_record svr where svr.vacancy_id=v.id and svr.seeker_id=:id) as count" +
                " from vacancies as v " +
                " left join profile p on v.creator_profile_id=p.id")
                .setParameter("id", profileId).getResultList();

        ViewedVacanciesDTO dto = new ViewedVacanciesDTO();
        for (Object vacancy : vacs) {
            dto.setId(Long.parseLong(((Object[]) vacancy)[0].toString()));
            dto.setHeadline((String) ((Object[]) vacancy)[1]);
            dto.setSalarymin(Integer.parseInt(((Object[]) vacancy)[2].toString()));
            dto.setSalarymax(Integer.parseInt(((Object[]) vacancy)[3].toString()));
            dto.setCompanyname((String) ((Object[]) vacancy)[4]);
            dto.setViews(Integer.parseInt(((Object[]) vacancy)[5].toString()));
        }
        vacanciesDTOList.add(dto);
        return vacanciesDTOList;
    }
}
