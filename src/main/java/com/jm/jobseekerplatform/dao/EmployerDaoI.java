package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.users.UserEmployer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerDaoI extends JpaRepository<UserEmployer,Long> {
    Page<UserEmployer> findAll(Pageable pageable);
}
