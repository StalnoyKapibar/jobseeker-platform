package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.interfaces.SeekerVacancyRecordDao;
import com.jm.jobseekerplatform.dto.ViewedVacanciesDTO;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public abstract class SeekerVacanceRecordDaoImpl implements SeekerVacancyRecordDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ViewedVacanciesDTO> getNumberOfViewsOffAllVacanciesByTagForSeeker(SeekerProfile seekerProfile) {
        Long profileId = seekerProfile.getId();
        List<ViewedVacanciesDTO> vacanciesDTOList = new ArrayList();
        List vacs = entityManager.createNativeQuery("select v.id, v.headline, " +
                "IFNULL(v.salarymin, 0) as salarymin, " +
                "IFNULL(v.salarymax, 0) as salarymax, p.companyname,  " +
                "(select count(*) from seeker_vacancy_record svr where svr.vacancy_id=v.id and svr.seeker_id=:id) " +
                "as count" +
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
