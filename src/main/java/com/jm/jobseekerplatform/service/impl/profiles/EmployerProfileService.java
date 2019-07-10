package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.impl.profiles.EmployerProfileDAO;
import com.jm.jobseekerplatform.model.profiles.ProfileEmployer;
import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service("employerProfileService")
@Transactional
public class EmployerProfileService extends AbstractService<ProfileEmployer> {

    @Autowired
    private EmployerProfileDAO dao;

    public void blockPermanently(ProfileEmployer profileEmployer) {
        profileEmployer.setState(State.BLOCK_PERMANENT);
        profileEmployer.setExpiryBlock(null);
        dao.update(profileEmployer);
    }

    public void blockTemporary(ProfileEmployer profileEmployer, int periodInDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        Date expiryBlockDate = calendar.getTime();

        profileEmployer.setState(State.BLOCK_TEMPORARY);
        profileEmployer.setExpiryBlock(expiryBlockDate);
        dao.update(profileEmployer);
    }

    public void blockOwn(ProfileEmployer profileEmployer) {
        profileEmployer.setState(State.BLOCK_OWN);
        profileEmployer.setExpiryBlock(null);
        dao.update(profileEmployer);
    }

    public void unblock(ProfileEmployer profileEmployer) {
        profileEmployer.setState(State.ACCESS);
        profileEmployer.setExpiryBlock(null);
        dao.update(profileEmployer);
    }

    public int deletePermanentBlockEmployerProfiles() {
        return dao.deletePermanentBlockEmployerProfiles();
    }

    public int deleteExpiryBlockEmployerProfiles() {
        return dao.deleteExpiryBlockEmployerProfiles();
    }
}
