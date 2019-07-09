package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.users.UserSeeker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeekerDaoI extends JpaRepository<UserSeeker,Long> {
    Page<UserSeeker> findAll(Pageable pageable);
}
