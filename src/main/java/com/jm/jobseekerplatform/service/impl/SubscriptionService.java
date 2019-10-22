package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.SubscriptionDAO;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SubscriptionService")
public class SubscriptionService extends AbstractService<Subscription> {

    @Autowired
    private SubscriptionDAO subscriptionDAO;

    public Subscription findBySeekerAndEmployer(SeekerProfile seekerProfile, EmployerProfile employerProfile) {
        return subscriptionDAO.findBySeekerAndEmployer(seekerProfile, employerProfile);
    }

    public void deleteSubscription(Subscription subscription) {
        subscriptionDAO.deleteSubscription(subscription);
    }

}