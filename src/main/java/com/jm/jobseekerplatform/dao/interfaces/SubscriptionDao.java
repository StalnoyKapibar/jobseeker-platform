package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionDao extends JpaRepository<Subscription, Long> {

    Subscription findBySeekerAndEmployer(SeekerProfile seekerProfile, EmployerProfile employerProfile);
}