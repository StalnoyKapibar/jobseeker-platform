package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.JobExperienceDAO;
import com.jm.jobseekerplatform.model.JobExperience;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class JobExperienceService extends AbstractService<JobExperience> {

    @Autowired
    private JobExperienceDAO jobExperienceDAO;

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
                jobExperienceDAO.add(jobExperience);
            } else {
                jobExperienceDAO.update(jobExperience);
            }
        }
        return jobExperiences;
    }

    public Set<Long> getAllExperiencesIdForResume(Long id){
        return jobExperienceDAO.getAllExperiencesIdForResume(id);
    }
}
