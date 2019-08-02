package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface NewsDaoI extends JpaRepository<News, Long> {
    Page<News> findAll(Pageable pageable);

    @Query(value = "SELECT distinct n FROM News n JOIN n.author t WHERE t IN ?1")
    Page<News> getAllByEmployerProfileId(EmployerProfile employerProfile, Pageable pageable);

    @Query(value = "SELECT distinct n FROM News n JOIN n.author t WHERE t IN ?1")
    Page<News> getAllBySeekerProfileId(Set<EmployerProfile> employerProfiles, Pageable pageable);
}
