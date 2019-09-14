package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.StoredProcedureDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StoredProcedureService {
    @Autowired
    StoredProcedureDAO storedProcedureDAO;

    public void createSortVacancyProcedure() {
        storedProcedureDAO.createSortVacancyProcedure();
    }
}
