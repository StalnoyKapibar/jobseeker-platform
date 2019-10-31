package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.interfaces.profiles.EmployerProfileDao;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.service.AbstractService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service("employerProfileService")
@Transactional
public class EmployerProfileService extends AbstractService<EmployerProfile> {

    @Autowired
    private EmployerProfileDao employerProfileDao;

    @Autowired
    private EmployerUserService employerUserService;

    public int deletePermanentBlockEmployerProfiles() {
        return employerProfileDao.deleteByState_BlockPermanent();
    }

    public int deleteExpiryBlockEmployerProfiles() {
        return employerProfileDao.deleteExpiryBlockEmployerProfiles();
    }

    public void updatePhoto(long id, MultipartFile file) {
        EmployerUser employerUser = employerUserService.getByProfileId(id);
        if (!file.isEmpty()) {
            try {
                byte[] photo = file.getBytes();
                employerUser.getProfile().setLogo(photo);
                update(employerUser.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<EmployerProfile> getProfilesExpNotNull() {
        return employerProfileDao.findAllByExpiryBlockIsNotNull();
    }

}
