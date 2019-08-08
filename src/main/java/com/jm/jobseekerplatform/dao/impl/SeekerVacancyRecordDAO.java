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
        List<Map<String, String>> listVacancies = new ArrayList<>();
        List vacancies = entityManager
                .createQuery("select distinct s.vacancy.id, s.vacancy.headline from SeekerVacancyRecord s where s.seeker.id=:id")
                .setParameter("id", seekerId).getResultList();
        for(Object vacancy : vacancies) {
            Map<String, String> mapVac = new HashMap();
            mapVac.put("id", Long.toString((Long)((Object[]) vacancy)[0]));
            mapVac.put("headline", (String)((Object[]) vacancy)[1]);
            listVacancies.add(mapVac);
        }
        return listVacancies;
    }
}
