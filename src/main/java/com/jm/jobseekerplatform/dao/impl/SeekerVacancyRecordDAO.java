package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ViewedVacanciesDTO;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SeekerVacancyRecordDAO extends AbstractDAO<SeekerVacancyRecord> {

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

    public List<ViewedVacanciesDTO> getAllViewedVacanciesBySeeker(Long seekerId) {
        List<ViewedVacanciesDTO> listVacancies = new ArrayList();
        List vacancies = entityManager
                .createNativeQuery(query_for_get_all_viewed_vacancies)
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
}
