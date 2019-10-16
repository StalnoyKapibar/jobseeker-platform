package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployerUserDaoI extends JpaRepository<EmployerUser,Long> {
    Page<EmployerUser> findAll(Pageable pageable);

    @Query(value = "SELECT distinct e FROM EmployerUser e where e.date between :startDate and :endDate")
    List<EmployerUser> getEmployerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate);
}
