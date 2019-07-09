package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.EmployerProfile;
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
}
