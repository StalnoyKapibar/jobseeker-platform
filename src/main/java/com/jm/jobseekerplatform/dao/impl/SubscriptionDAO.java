package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Repository("SubscriptionDAO")
public class SubscriptionDAO extends AbstractDAO<Subscription> {

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

    public void deleteSubscription(Subscription subscription) {

        Long id = subscription.getId();
        List<String> queries = new ArrayList<>();
        queries.add("delete from profile_subscriptions where subscriptions_id=:id");
        queries.add("delete from subscription_tags where subscription_id=:id");
        queries.add("delete from subscription where id=:id");

        queries.forEach(
                query -> entityManager
                        .createNativeQuery(query)
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }
}