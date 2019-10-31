package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.JobExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Set;

@Repository
public interface JobExperienceDao extends JpaRepository<JobExperience, Long> {

    @Query(value = "select job_experiences_id from resumes_job_experiences where " +
            "resume_id = :id", nativeQuery = true)
    Set<BigInteger> getAllExperiencesIdForResume(@Param("id") Long id);
}
