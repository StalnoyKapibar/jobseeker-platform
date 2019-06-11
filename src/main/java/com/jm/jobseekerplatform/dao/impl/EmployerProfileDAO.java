package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository("employerProfileDAO")
public class EmployerProfileDAO extends AbstractDAO<EmployerProfile> {

    public int deletePermanentBlockEmployerProfiles() {
        int deletedCount = entityManager.createQuery("DELETE FROM EmployerProfile ep WHERE ep.state = 'BLOCK_PERMANENT'").executeUpdate();
        return deletedCount;
    }

    public int deleteExpiryBlockEmployerProfiles() {
        Date currentDate = new Date();
        int deletedCount = entityManager.createQuery("DELETE FROM EmployerProfile ep WHERE ep.expiryBlock <= :param")
                .setParameter("param", currentDate)
                .executeUpdate();
        return deletedCount;
    }

    public void addVacancyToEmployerProfile(Long employerProfileId, Long vacancyId) {
        entityManager
                .createNativeQuery("INSERT INTO employerprofiles_vacancies (employer_profile_id, vacancies_id) values (?, ?)")
                .setParameter(1, employerProfileId)
                .setParameter(2, vacancyId)
                .executeUpdate();
    }

    public Optional<EmployerProfile> getByVacancyId(Long vacancyId) {

        EmployerProfile employerProfile = entityManager.createQuery(
                "select e from EmployerProfile e " +
                        "join e.vacancies v " +
                        "where v.id = :vacancyId", EmployerProfile.class)
                .setParameter("vacancyId", vacancyId)
                .getSingleResult();


        return Optional.ofNullable(employerProfile);
    }
}
