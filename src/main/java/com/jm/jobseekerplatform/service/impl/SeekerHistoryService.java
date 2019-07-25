package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.SeekerVacancyRecordDAO;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeekerHistoryService extends AbstractService<SeekerVacancyRecord> {

    @Autowired
    SeekerVacancyRecordDAO seekerVacancyRecordDAO;
}
