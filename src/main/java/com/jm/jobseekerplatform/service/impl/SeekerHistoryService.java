package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.SeekerVacancyRecordDAO;
import com.jm.jobseekerplatform.dto.ViewedVacanciesDTO;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeekerHistoryService extends AbstractService<SeekerVacancyRecord> {

    @Autowired
    SeekerVacancyRecordDAO seekerVacancyRecordDAO;

    public List<ViewedVacanciesDTO> getViewedVacanciesBySeeker(Long seekerId) {
        return seekerVacancyRecordDAO.getViewedVacanciesBySeeker(seekerId);
    }

    public List<ViewedVacanciesDTO> getNumberOfViewsOffAllVacanciesByTagForSeeker(SeekerProfile seekerProfile) {
        return seekerVacancyRecordDAO.getNumberOfViewsOffAllVacanciesByTagForSeeker(seekerProfile);
    }
}
