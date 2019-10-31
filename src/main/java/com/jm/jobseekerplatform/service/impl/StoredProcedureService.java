package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.StoredProcedureDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StoredProcedureService {
    @Autowired
    StoredProcedureDao storedProcedureDao;

    public void createSortVacancyProcedure() {
        storedProcedureDao.createSortVacancyProcedure();
    }
}
