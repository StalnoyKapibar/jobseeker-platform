package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.users.EmployerUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerUserDaoI extends JpaRepository<EmployerUser,Long> {
    Page<EmployerUser> findAll(Pageable pageable);
}
