package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDao extends JpaRepository<News, Long> {

    Page<News> getBySubscriptions(Iterable<Subscription> subscriptions, Pageable pageable);

    Page<News> getBySeekerProfileTags(SeekerProfile profile, Pageable pageable);

    @Query(value = "SELECT distinct n FROM News n JOIN n.author t WHERE t IN ?1")
    Page<News> getAllByEmployerProfileId(EmployerProfile employerProfile, Pageable pageable);
}
