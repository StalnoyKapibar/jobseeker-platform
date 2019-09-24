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

@Service("employerProfileService")
@Transactional
public class EmployerProfileService extends AbstractService<EmployerProfile> {

    @Autowired
    private EmployerProfileDAO dao;

    @Autowired
    private EmployerUserService employerUserService;

    public int deletePermanentBlockEmployerProfiles() {
        return dao.deletePermanentBlockEmployerProfiles();
    }

    public int deleteExpiryBlockEmployerProfiles() {
        return dao.deleteExpiryBlockEmployerProfiles();
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
}
