package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.EmployerProfileDAO;
import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service("employerProfileService")
@Transactional
public class EmployerProfileService extends AbstractService<EmployerProfile> {

    @Autowired
    private EmployerProfileDAO dao;


    public EmployerProfile getByVacancyId(Long vacancyId) {
        return dao.findEmployerProfileByVacancyId(vacancyId);
    }

    public void blockPermanently(EmployerProfile employerProfile) {
        employerProfile.setState(State.BLOCK_PERMANENT);
        employerProfile.setExpiryBlock(null);
        dao.update(employerProfile);
    }

    public void blockTemporary(EmployerProfile employerProfile, int periodInDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        Date expiryBlockDate = calendar.getTime();

        employerProfile.setState(State.BLOCK_TEMPORARY);
        employerProfile.setExpiryBlock(expiryBlockDate);
        dao.update(employerProfile);
    }

    public void blockOwn(EmployerProfile employerProfile) {
        employerProfile.setState(State.BLOCK_OWN);
        employerProfile.setExpiryBlock(null);
        dao.update(employerProfile);
    }

    public void unblock(EmployerProfile employerProfile) {
        employerProfile.setState(State.ACCESS);
        employerProfile.setExpiryBlock(null);
        dao.update(employerProfile);
    }

    public int deletePermanentBlockEmployerProfiles() {
        return dao.deletePermanentBlockEmployerProfiles();
    }

    public int deleteExpiryBlockEmployerProfiles() {
        return dao.deleteExpiryBlockEmployerProfiles();
    }

    public void addVacancyToEmployerProfile(Long employerProfileId, Long vacancyId) {
        dao.addVacancyToEmployerProfile(employerProfileId, vacancyId);
    }
}
