package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SeekerVacancyRecordDAO extends AbstractDAO<SeekerVacancyRecord> {

    public List<Map<String, String>> getViewedVacanciesBySeeker(Long seekerId) {
        List<Map<String, String>> listVacancies = new ArrayList();
        List vacancies = entityManager
                .createNativeQuery("select distinct v.id, v.headline, IFNULL(v.salarymin, 0) as salarymin, IFNULL(v.salarymax, 0) as salarymax, p.companyname  from seeker_vacancy_record as s left join vacancies as v on s.vacancy_id=v.id  left join profile p on v.creator_profile_id=p.id where s.seeker_id=:id")
                .setParameter("id", seekerId).getResultList();

        for(Object vacancy : vacancies) {
            Map<String, String> mapVac = new HashMap();
            mapVac.put("id", (((Object[]) vacancy)[0]).toString());
            mapVac.put("headline", (String)((Object[]) vacancy)[1]);
            mapVac.put("salarymin", (((Object[]) vacancy)[2]).toString());
            mapVac.put("salarymax", (((Object[]) vacancy)[3]).toString());
            mapVac.put("companyname", (String)((Object[]) vacancy)[4]);
            listVacancies.add(mapVac);
        }
        return listVacancies;
    }
}
