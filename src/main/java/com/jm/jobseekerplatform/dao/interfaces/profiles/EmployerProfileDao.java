package com.jm.jobseekerplatform.dao.interfaces.profiles;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployerProfileDao extends JpaRepository<EmployerProfile, Long> {

    @Query("select e from EmployerProfile e where e.vacancies.id = :vacancyId")
    EmployerProfile findByVacancyId(@Param("vacancyId") Long vacancyId);

    int deleteByState_BlockPermanent();

    public int deleteExpiryBlockEmployerProfiles();

    List<EmployerProfile> findAllByExpiryBlockIsNotNull();

    @Query("SELECT u FROM User u WHERE u.profile = :profile")
    User findByEmployerProfile(@Param("profile") EmployerProfile employerProfile);
}
