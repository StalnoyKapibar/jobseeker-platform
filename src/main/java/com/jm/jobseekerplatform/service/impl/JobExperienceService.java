package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.JobExperienceDao;
import com.jm.jobseekerplatform.model.JobExperience;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;

@Service
public class JobExperienceService extends AbstractService<JobExperience> {

    @Autowired
    private JobExperienceDao jobExperienceDao;

    public Set<JobExperience> validateJobExperiences(Set<JobExperience> jobExperiences) {
        for (JobExperience jobExperience : jobExperiences) {
            if (jobExperience == null ||
                    jobExperience.getCompanyName() == null ||
                    jobExperience.getPosition().isEmpty() ||
                    jobExperience.getResponsibilities().isEmpty() ||
                    jobExperience.getFirstWorkDay().equals("1970-01-01 00:00:00.000000") ||
                    jobExperience.getLastWorkDay().equals("1970-01-01 00:00:00.000000")) {
                jobExperiences.remove(jobExperience);
                continue;
            }
            if (jobExperience.getId() == null) {
                jobExperienceDao.save(jobExperience);
            } else {
                jobExperienceDao.save(jobExperience);
            }
        }
        return jobExperiences;
    }

    public Set<BigInteger> getAllExperiencesIdForResume(Long id) {
        return jobExperienceDao.getAllExperiencesIdForResume(id);
    }
}
