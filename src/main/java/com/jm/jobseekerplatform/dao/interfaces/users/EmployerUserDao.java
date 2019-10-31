package com.jm.jobseekerplatform.dao.interfaces.users;

import com.jm.jobseekerplatform.model.users.EmployerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployerUserDao extends JpaRepository<EmployerUser, Long> {

    @Query("select e from  EmployerUser e where e.profile.id = :employerProfileId")
    EmployerUser findByProfileId(@Param("employerProfileId")Long employerProfileId);

    @Query( "SELECT distinct e FROM EmployerUser e where e.date between :startDate and :endDate")
    List<EmployerUser> getEmployerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate);

}
