package com.jm.jobseekerplatform.dao.impl.profiles;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("employerProfileDAO")
public class EmployerProfileDAO extends AbstractDAO<EmployerProfile> {

    public int deletePermanentBlockEmployerProfiles() {
        int deletedCount = entityManager.createQuery("DELETE FROM EmployerProfile ep WHERE ep.state = 'BLOCK_PERMANENT'").executeUpdate(); //todo check table EmployerProfile
        return deletedCount;
    }

    public int deleteExpiryBlockEmployerProfiles() {
        Date currentDate = new Date();
        int deletedCount = entityManager.createQuery("DELETE FROM EmployerProfile ep WHERE ep.expiryBlock <= :param") //todo check table EmployerProfile
                .setParameter("param", currentDate)
                .executeUpdate();
        return deletedCount;
    }
}
