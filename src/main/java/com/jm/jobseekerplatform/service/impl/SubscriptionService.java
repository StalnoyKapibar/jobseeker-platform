package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.SubscriptionDao;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SubscriptionService")
public class SubscriptionService extends AbstractService<Subscription> {

    @Autowired
    private SubscriptionDao subscriptionDao;

    public Subscription findBySeekerAndEmployer(SeekerProfile seekerProfile, EmployerProfile employerProfile) {
        return subscriptionDao.findBySeekerAndEmployer(seekerProfile, employerProfile);
    }

    public void deleteSubscription(Subscription subscription) {
        subscriptionDao.delete(subscription);
    }
}