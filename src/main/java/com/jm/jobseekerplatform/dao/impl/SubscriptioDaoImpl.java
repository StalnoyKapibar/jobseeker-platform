package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.interfaces.SubscriptionDao;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public abstract class SubscriptioDaoImpl implements SubscriptionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Subscription findBySeekerAndEmployer(SeekerProfile seekerProfile, EmployerProfile employerProfile) {
        Subscription subscription = null;

        try {
            subscription = entityManager
                    .createQuery("select s from Subscription s where s.employerProfile =:employer and s.seekerProfile =:seeker", Subscription.class)
                    .setParameter("employer", employerProfile)
                    .setParameter("seeker", seekerProfile)
                    .getSingleResult();
            return subscription;
        } catch (NoResultException e) {
            return subscription;
        }
    }
}
