package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.SeekerVacancyRecordDao;
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
    SeekerVacancyRecordDao seekerVacancyRecordDao;

    public List<ViewedVacanciesDTO> getViewedVacanciesBySeeker(Long seekerId) {
        return seekerVacancyRecordDao.findAllBySeeker_Id(seekerId);
    }

    public List<ViewedVacanciesDTO> getNumberOfViewsOffAllVacanciesByTagForSeeker(SeekerProfile seekerProfile) {
        return seekerVacancyRecordDao.getNumberOfViewsOffAllVacanciesByTagForSeeker(seekerProfile);
    }

}
