package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.JobExperience;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Repository("jobExperienceDAO")
public class JobExperienceDAO extends AbstractDAO<JobExperience> {

    public Set<Long> getAllExperiencesIdForResume(Long id){
        Query query = entityManager.createNativeQuery("select job_experiences_id from resumes_job_experiences where " +
                "resume_id = :id").setParameter("id", id);
        Set<Long> experiencesId = new HashSet<>();
        query.getResultList().forEach(element ->
                experiencesId.add(((BigInteger)element).longValue())
        );
        return experiencesId;
    }
}
