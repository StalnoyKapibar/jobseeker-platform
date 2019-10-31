package com.jm.jobseekerplatform.dao.interfaces.users;

import com.jm.jobseekerplatform.model.users.SeekerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeekerUserDao extends JpaRepository<SeekerUser, Long> {

    @Query("select e from  SeekerUser e where e.profile.id = :seekerProfileId")
    SeekerUser findByProfileId(@Param("seekerProfileId") Long seekerProfileId);

    @Query("SELECT distinct e FROM SeekerUser e where e.date between :startDate and :endDate")
    List<SeekerUser> getSeekerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate);
}
