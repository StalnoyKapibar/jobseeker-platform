package com.jm.jobseekerplatform.dao.impl.profiles;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import org.springframework.stereotype.Repository;

@Repository("employerProfileDAO")
public class EmployerProfileDAO extends AbstractDAO<EmployerProfile> {

    public EmployerProfile findEmployerProfileByVacancyId(Long vacancyId) {
        return entityManager.createQuery("select e from EmployerProfile e where e.vacancies.id = :vacancyId", EmployerProfile.class)
                .setParameter("vacancyId", vacancyId)
                .getSingleResult();
    }
}
