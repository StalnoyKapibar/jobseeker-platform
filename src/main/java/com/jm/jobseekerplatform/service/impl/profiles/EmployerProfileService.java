package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.impl.profiles.EmployerProfileDAO;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.AbstractService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;

@Service("employerProfileService")
@Transactional
public class EmployerProfileService extends AbstractService<EmployerProfile> {

    @Autowired
    private EmployerProfileDAO dao;
    @Autowired
    private EmployerUserService employerUserService;
    @Autowired
    private EmployerProfileService employerProfileService;

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

    public void updatePhoto(long id, MultipartFile file){
        EmployerUser employerUser = employerUserService.getById(id);
        if (!file.isEmpty()) {
            try {
                byte[] logo = file.getBytes();
                employerUser.getProfile().setLogo(logo);
                employerProfileService.update(employerUser.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
