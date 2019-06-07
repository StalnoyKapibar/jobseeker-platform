package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Seeker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeekerDaoI extends JpaRepository<Seeker,Long> {
    Page<Seeker> findAll(Pageable pageable);
}
