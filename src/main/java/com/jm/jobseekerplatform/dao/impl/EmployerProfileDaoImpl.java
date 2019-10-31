package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.interfaces.profiles.EmployerProfileDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

public abstract class EmployerProfileDaoImpl implements EmployerProfileDao {

    @PersistenceContext
    private EntityManager entityManager;

    public int deleteExpiryBlockEmployerProfiles() {
        Date currentDate = new Date();
        int deletedCount = entityManager.createQuery("DELETE FROM EmployerProfile ep " +
                "WHERE ep.expiryBlock <= :param")
                .setParameter("param", currentDate)
                .executeUpdate();
        return deletedCount;
    }
}
