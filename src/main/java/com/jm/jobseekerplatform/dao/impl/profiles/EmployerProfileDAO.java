package com.jm.jobseekerplatform.dao.impl.profiles;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("employerProfileDAO")
public class EmployerProfileDAO extends AbstractDAO<EmployerProfile> {


    public EmployerProfile findEmployerProfileByVacancyId(Long vacancyId) {
        return entityManager.createQuery("select e from EmployerProfile e where e.vacancies.id = :vacancyId", EmployerProfile.class)
                .setParameter("vacancyId", vacancyId)
                .getSingleResult();
    }

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

	public EmployerProfile getEmployerProfileByVacancyID(long id) {
		return entityManager.createQuery("SELECT v FROM EmployerProfile v JOIN v.vacancies r WHERE r.id =:param", EmployerProfile.class)
				.setParameter("param", id)
				.getSingleResult();
	}

}
