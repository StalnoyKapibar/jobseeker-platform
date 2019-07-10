package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.users.SeekerUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeekerUserDaoI extends JpaRepository<SeekerUser,Long> {
    Page<SeekerUser> findAll(Pageable pageable);
}
